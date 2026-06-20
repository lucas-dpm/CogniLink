package com.lucasdpm.cognilink.domain.usecase

import android.util.Log
import com.lucasdpm.cognilink.domain.model.ValidationResult
import com.lucasdpm.cognilink.domain.repository.AIService
import com.lucasdpm.cognilink.domain.repository.NetworkMonitor
import com.lucasdpm.cognilink.domain.service.AppNotificationService

class ValidateBasicAnswerUseCase(
    private val aiService: AIService,
    private val networkMonitor: NetworkMonitor,
    private val notificationService: AppNotificationService
) {
    companion object {
        private const val TAG = "ValidateBasicAnswerUseCase"
    }

    suspend operator fun invoke(
        question: String,
        userAnswer: String,
        correctAnswer: String
    ): ValidationResult {
        Log.d(TAG, "Iniciando validação. Questão: $question")
        Log.d(TAG, "Resposta do usuário: $userAnswer")
        Log.d(TAG, "Resposta correta esperada: $correctAnswer")

        if (networkMonitor.isOnline()) {
            Log.d(TAG, "Dispositivo online. Tentando validação via IA...")
            val result = aiService.compareAnswer(question, correctAnswer, userAnswer)
            result.onSuccess { feedback ->
                Log.d(TAG, "IA respondeu. Correto: ${feedback.isCorrect}, Dica: ${feedback.tip}")
                return if (feedback.isCorrect) {
                    ValidationResult.Correct(feedback.tip)
                } else {
                    ValidationResult.Fallback(feedback.tip)
                }
            }.onFailure { error ->
                Log.e(TAG, "Erro na chamada da IA", error)
                notificationService.showWarning("Erro ao conectar com a IA. Usando validação local.")
            }
        } else {
            Log.d(TAG, "Dispositivo offline. Lançando aviso e seguindo para fallback local.")
            notificationService.showWarning("Você está offline. A validação será feita localmente.")
        }

        // Fallback para comparação local se não houver internet ou se a chamada falhar
        val isCorrectLocal = userAnswer.trim().equals(correctAnswer.trim(), ignoreCase = true)
        Log.d(TAG, "Executando fallback local. Resultado: $isCorrectLocal")

        return if (isCorrectLocal) {
            ValidationResult.Correct()
        } else {
            ValidationResult.Fallback()
        }
    }
}
