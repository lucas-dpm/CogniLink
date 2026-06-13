package com.lucasdpm.cognilink.domain.usecase

import com.lucasdpm.cognilink.data.model.Flashcard
import com.lucasdpm.cognilink.domain.model.DifficultyLevel

class CalculateDifficultyLevelUseCase {

    operator fun invoke(flashcards: List<Flashcard>): DifficultyLevel{
        if (flashcards.isEmpty()) return DifficultyLevel.EASY
        return DifficultyLevel.fromAverage(flashcards.map{it.difficulty.weight}.average().toFloat())
    }
}