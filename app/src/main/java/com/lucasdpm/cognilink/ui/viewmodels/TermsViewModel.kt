package com.lucasdpm.cognilink.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasdpm.cognilink.data.repository.TermsRepository
import com.lucasdpm.cognilink.domain.service.AppNotificationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TermsViewModel(
    private val repository: TermsRepository,
    private val notificationService: AppNotificationService
) : ViewModel() {

    companion object {
        private const val TAG = "TermsViewModel"
    }

    var termsText by mutableStateOf("Carregando...")
        private set

    init {
        loadTerms()
    }

    fun refreshTerms() {
        loadTerms()
    }

    private fun loadTerms() {
        viewModelScope.launch {
            termsText = "Carregando..."
            try {
                val content = withContext(Dispatchers.IO) {
                    repository.getTerms()
                }
                termsText = content
            } catch (e: Exception) {
                termsText = "Erro ao carregar termos de uso."
                notificationService.showError("Falha ao carregar termos.")
                Log.e(TAG, "loadTerms: Erro ao carregar termos de uso", e)
            }
        }
    }
}
