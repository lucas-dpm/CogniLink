package com.lucasdpm.cognilink.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdpm.cognilink.data.model.Answer
import com.lucasdpm.cognilink.data.model.Flashcard
import com.lucasdpm.cognilink.data.repository.FlashcardRepository
import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.domain.model.FlashcardType
import com.lucasdpm.cognilink.domain.repository.AIService
import com.lucasdpm.cognilink.domain.repository.NetworkMonitor
import com.lucasdpm.cognilink.domain.service.AppNotificationService
import com.lucasdpm.cognilink.ui.states.IAGeneratorUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IAGeneratorViewModel(
    private val aiService: AIService,
    private val flashcardRepository: FlashcardRepository,
    private val notificationService: AppNotificationService,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    companion object {
        private const val TAG = "IAGeneratorViewModel"
    }

    private val _uiState = MutableStateFlow(IAGeneratorUiState())
    val uiState: StateFlow<IAGeneratorUiState> = _uiState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<IAGeneratorNavigationEvent>()
    val navigationEvents: SharedFlow<IAGeneratorNavigationEvent> = _navigationEvents.asSharedFlow()

    fun initialize(deckId: String) {
        if (_uiState.value.deckId == deckId) return
        _uiState.update { it.copy(deckId = deckId) }
    }

    fun onThemeChange(newTheme: String) {
        _uiState.update { it.copy(flashcardTheme = newTheme) }
    }

    fun onQuantityChange(newQuantity: Int) {
        _uiState.update { it.copy(quantity = newQuantity) }
    }

    fun onDifficultyChange(newDifficulty: DifficultyLevel?) {
        _uiState.update { it.copy(selectedDifficulty = newDifficulty) }
    }

    fun onTypeChange(newType: FlashcardType?) {
        _uiState.update { it.copy(selectedType = newType) }
    }

    fun onTopicsUpdate(newTopics: List<String>) {
        _uiState.update { it.copy(topics = newTopics) }
    }

    fun onFileSelected(uri: String, fileName: String, fileBytes: ByteArray?) {
        _uiState.update { 
            it.copy(
                selectedFileUri = uri,
                selectedFileName = fileName,
                hasFile = true
            )
        }

        if (fileBytes != null) {
            analyzeDocument(fileBytes, fileName)
        }
    }

    private fun analyzeDocument(fileBytes: ByteArray, fileName: String) {
        if (!networkMonitor.isOnline()) {
            viewModelScope.launch {
                notificationService.showError("Você está offline. Conecte-se à internet para analisar o documento.")
            }
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val result = aiService.analyzeDocument(fileBytes, fileName)
            result.onSuccess { analysis ->
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                        flashcardTheme = analysis.mainTheme,
                        topics = analysis.topics
                    )
                }
            }.onFailure { error ->
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                    ) 
                }
                viewModelScope.launch {
                    notificationService.showError("Erro ao analisar documento. Por favor, tente novamente mais tarde!")
                }
                Log.e(TAG, "analyzeDocument: Erro ao analisar documento", error)
            }
        }
    }

    private fun validate(): Boolean {
        val state = _uiState.value
        if (state.flashcardTheme.isBlank() && !state.hasFile) {
            viewModelScope.launch {
                notificationService.showError("Forneça um tema ou anexe um arquivo.")
            }
            return false
        }
        return true
    }

    fun generateFlashcards() {
        if (!validate()) return
        
        if (!networkMonitor.isOnline()) {
            viewModelScope.launch {
                notificationService.showError("Você está offline. Conecte-se à internet para gerar flashcards com IA.")
            }
            return
        }

        val state = _uiState.value
        val deckId = state.deckId ?: return
        
        _uiState.update { it.copy(isLoading = true) }
        
        viewModelScope.launch {
            val result = aiService.generateFlashcardsWithIA(
                mainTheme = state.flashcardTheme,
                topics = state.topics,
                difficulty = state.selectedDifficulty?.name ?: "RANDOM",
                type = state.selectedType?.name ?: "RANDOM",
                quantity = state.quantity
            )

            result.onSuccess { generatedList ->
                val flashcards = generatedList.map { generated ->
                    Flashcard(
                        deckId = deckId,
                        question = generated.question,
                        cardType = generated.type,
                        difficulty = generated.difficulty,
                        hints = generated.hints,
                        answerOptions = generated.answerOptions.map { 
                            Answer(it.answer, isCorrect = it.isCorrect) 
                        }
                    )
                }
                
                flashcardRepository.saveAllFlashcards(flashcards)
                _uiState.update { it.copy(isLoading = false) }
                notificationService.showSuccess("${flashcards.size} flashcards gerados com sucesso!")
                _navigationEvents.emit(IAGeneratorNavigationEvent.NavigateBack)
            }.onFailure { error ->
                notificationService.showError("Erro ao gerar flashcards. Por favor, tente novamente mais tarde!")
                Log.e(TAG, "generateFlashcards: Erro ao gerar flashcards", error)
                _uiState.update { 
                    it.copy(
                        isLoading = false,
                    ) 
                }
            }
        }
    }
}

sealed class IAGeneratorNavigationEvent {
    object NavigateBack : IAGeneratorNavigationEvent()
}
