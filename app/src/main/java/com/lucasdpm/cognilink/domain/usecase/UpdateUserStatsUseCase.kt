package com.lucasdpm.cognilink.domain.usecase

import android.util.Log
import com.lucasdpm.cognilink.data.model.UserStats
import com.lucasdpm.cognilink.data.repository.FlashcardRepository
import com.lucasdpm.cognilink.data.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull

class UpdateUserStatsUseCase(
    private val userRepository: UserRepository,
    private val flashcardRepository: FlashcardRepository,
    private val calculateUserRankingUseCase: CalculateUserRankingUseCase
) {
    companion object {
        private const val TAG = "UpdateUserStatsUseCase"
    }

    suspend operator fun invoke(userId: String) {
        try {
            val currentStats = userRepository.getUserStats(userId).firstOrNull() ?: UserStats(userId = userId)
            val allFlashcardStats = flashcardRepository.getAllStatisticsForUser(userId)

            if (allFlashcardStats.isEmpty()) return

            val totalHits = allFlashcardStats.sumOf { it.hits }
            val totalMisses = allFlashcardStats.sumOf { it.misses }
            val totalStudyTime = allFlashcardStats.sumOf { it.studyTime }
            val totalDone = allFlashcardStats.count { it.hits > 0 || it.misses > 0 }
            
            // Média global de latência (apenas para cards que já foram respondidos)
            val cardsWithLatency = allFlashcardStats.filter { it.hits + it.misses > 0 }
            val globalAverageLatency = if (cardsWithLatency.isNotEmpty()) {
                cardsWithLatency.map { it.averageLatencyMs }.average().toLong()
            } else 0L

            // Taxa de retenção global
            val totalAttempts = totalHits + totalMisses
            val globalRetentionRate = if (totalAttempts > 0) {
                totalHits.toFloat() / totalAttempts.toFloat()
            } else 0f

            // Domínio médio global
            val averageMastery = allFlashcardStats.map { it.mastery }.average().toFloat()

            // Eficiência Cognitiva (Exemplo: Acertos por minuto de estudo)
            val studyTimeMinutes = totalStudyTime / 60000f
            val efficiencyIndex = if (studyTimeMinutes > 0) {
                totalHits.toFloat() / studyTimeMinutes
            } else 0f

            val updatedStats = currentStats.copy(
                totalFlashcardsHits = totalHits,
                totalFlashcardsMisses = totalMisses,
                totalStudyTime = totalStudyTime,
                totalFlashcardsDone = totalDone,
                globalAverageLatencyMs = globalAverageLatency,
                retentionRate = globalRetentionRate,
                overallMastery = averageMastery,
                cognitiveEfficiencyIndex = efficiencyIndex,
                lastReview = System.currentTimeMillis()
            )

            // Calcula o ranking atualizado baseado nos novos stats
            val rankingResult = calculateUserRankingUseCase(updatedStats)
            val finalStats = updatedStats.copy(ranking = rankingResult)
            
            val user = userRepository.getUserById(userId)
            if (user != null) {
                userRepository.updateUser(user.copy(stats = finalStats))
            }
        } catch (e: Exception) {
            Log.e(TAG, "invoke: Erro ao atualizar estatísticas do usuário para o ID: $userId", e)
        }
    }
}
