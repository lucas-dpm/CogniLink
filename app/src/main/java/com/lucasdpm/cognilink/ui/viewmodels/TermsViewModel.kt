package com.lucasdpm.cognilink.ui.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lucasdpm.cognilink.data.repository.TermsRepository
import com.lucasdpm.cognilink.data.repository.TermsRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TermsViewModel(
    private val repository: TermsRepository
) : ViewModel() {

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
            }
        }
    }

    companion object {
        fun provideFactory(
            repository: TermsRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(TermsViewModel::class.java)) {
                    return TermsViewModel(repository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }

        fun provideFactory(context: Context): ViewModelProvider.Factory {
            val repository = TermsRepositoryImpl(context.applicationContext)
            return provideFactory(repository)
        }
    }
}
