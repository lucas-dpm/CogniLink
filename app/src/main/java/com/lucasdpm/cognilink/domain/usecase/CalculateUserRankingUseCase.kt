package com.lucasdpm.cognilink.domain.usecase

import com.lucasdpm.cognilink.data.model.UserStats
import com.lucasdpm.cognilink.domain.model.UserRank
import com.lucasdpm.cognilink.domain.model.UserRankingResult

class CalculateUserRankingUseCase {

    operator fun invoke(stats: UserStats): UserRankingResult {
        // 1. Métrica de Retenção e Domínio (Peso: 40%)
        // Baseado na taxa de retenção real e no domínio ponderado do sistema
        val masteryScore = stats.overallMastery * 100f // Assume overallMastery de 0.0 a 1.0
        val retentionScore = stats.retentionRate * 100f // Assume retentionRate de 0.0 a 1.0
        val memoryBase = ((masteryScore * 0.5f) + (retentionScore * 0.5f)) * 0.40f

        // 2. Eficiência Cognitiva (Peso: 25%)
        // Normalizamos o índice. Aqui assumimos que um índice de 5.0+ já é excelente.
        val normalizedEfficiency = (stats.cognitiveEfficiencyIndex / 5.0f).coerceIn(0.0f, 1.0f)
        val efficiencyBase = (normalizedEfficiency * 100f) * 0.25f

        // 3. Velocidade de Evocação / Automação (Peso: 20%)
        // Uma latência menor que 1.2s (1200ms) ganha nota máxima. Acima de 5s (5000ms) ganha zero.
        val maxLatency = 5000f
        val optimalLatency = 1200f
        val latencyScore = if (stats.globalAverageLatencyMs <= optimalLatency) {
            100f
        } else {
            val range = maxLatency - optimalLatency
            val userDiff = maxLatency - stats.globalAverageLatencyMs.toFloat()
            (userDiff / range).coerceIn(0.0f, 1.0f) * 100f
        }
        val latencyBase = latencyScore * 0.20f

        // 4. Inteligência Contextual (Peso: 15%)
        // Mistura a conversão de gatilhos com a consistência dos ambientes estudados
        val contextBase = (stats.contextTriggerConversionRate * 100f) * 0.15f

        // 5. PENALIDADE: O Limbo dos Sanguessugas (Leeches)
        // Cada card travado drena um pouco da consistência do ranking (-1.5 pontos por leech, teto de -15)
        val leechPenalty = (stats.activeLeechesCount * 1.5f).coerceAtMost(15.0f)

        // Cálculo da nota final (Garantindo o range de 0 a 100)
        val finalScore = (memoryBase + efficiencyBase + latencyBase + contextBase - leechPenalty)
            .coerceIn(0.0f, 100.0f)

        // Determinando o Rank atual
        val rank = UserRank.fromScore(finalScore)

        // 6. Geração da Microcopy Dinâmica (Insight Contextual)
        val insight = when {
            stats.activeLeechesCount > 5 ->
                "Seu potencial está alto, mas ${stats.activeLeechesCount} cartões 'Sanguessugas' estão travando seu avanço. Reestruture esses conceitos!"

            stats.globalAverageLatencyMs < 1500 && rank == UserRank.RECALL_MASTER ->
                "Incrível! Sua velocidade de evocação de ${(stats.globalAverageLatencyMs / 1000.0)}s indica conhecimento totalmente automatizado."

            stats.contextTriggerConversionRate > 0.8f ->
                "Você é altamente responsivo aos gatilhos de contexto! Isso está acelerando sua curva de aprendizado."

            else ->
                "Consistência ideal. Continue expandindo sua estabilidade de memória para alcançar a próxima patente."
        }

        return UserRankingResult(
            currentRank = rank,
            finalScore = finalScore,
            dynamicInsight = insight,
            efficiencyInsight = getCognitiveEfficiencyInsight(stats.cognitiveEfficiencyIndex),
            retentionInsight = getRetentionRateInsight(stats.retentionRate)
        )
    }

    private fun getCognitiveEfficiencyInsight(efficiencyIndex: Float): String {
        return when {
            efficiencyIndex > 0.8f -> "Seu cérebro está absorvendo mais conteúdo em menos tempo."
            efficiencyIndex > 0.5f -> "Você está mantendo um bom ritmo de aprendizado."
            else -> "Tente revisar conteúdos em sessões mais curtas para aumentar o foco."
        }
    }

    private fun getRetentionRateInsight(rate: Float): String {
        return when {
            rate > 0.9f -> "Excelente retenção! Você está dominando o conteúdo."
            rate > 0.7f -> "Boa retenção. Continue revisando para fixar o conhecimento."
            else -> "Considere revisar os cartões com maior frequência."
        }
    }
}