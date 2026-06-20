package com.lucasdpm.cognilink.domain.usecase

import com.lucasdpm.cognilink.domain.model.SM2Result
import java.util.Calendar
import kotlin.math.roundToInt

class CalculateSM2UseCase {
    /**
     * Calcula o próximo intervalo de revisão usando o algoritmo SM-2.
     * @param quality Qualidade da resposta (0-5).
     * @param previousInterval Intervalo anterior em dias.
     * @param previousEaseFactor Fator de facilidade anterior.
     * @param previousRepetitions Número de repetições consecutivas anteriores.
     */
    operator fun invoke(
        quality: Int,
        previousInterval: Float,
        previousEaseFactor: Float,
        previousRepetitions: Int
    ): SM2Result {
        var repetitions = previousRepetitions
        var easeFactor = previousEaseFactor
        val interval: Float

        if (quality >= 3) { // Sucesso
            interval = when (repetitions) {
                0 -> 1f
                1 -> 6f
                else -> (previousInterval * easeFactor)
            }
            repetitions++
        } else { // Falha
            repetitions = 0
            interval = 1f
        }

        // Atualização do Ease Factor (EF)
        // EF':=EF+(0.1-(5-q)*(0.08+(5-q)*0.02))
        easeFactor += (0.1f - (5 - quality) * (0.08f + (5 - quality) * 0.02f))
        
        // O Fator de Facilidade não deve ser inferior a 1.3
        if (easeFactor < 1.3f) easeFactor = 1.3f

        val nextReview = Calendar.getInstance().apply {
            // Adiciona o intervalo em dias. 
            // roundToInt() pois o SM-2 clássico usa dias inteiros, 
            // mas mantemos float para maior precisão em variações do algoritmo se necessário.
            add(Calendar.DAY_OF_YEAR, interval.roundToInt())
            set(Calendar.HOUR_OF_DAY, 4) // Define para as 4 da manhã para evitar problemas de fuso/dia
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        return SM2Result(
            intervalDays = interval,
            easeFactor = easeFactor,
            nextReview = nextReview,
            repetitions = repetitions
        )
    }
}
