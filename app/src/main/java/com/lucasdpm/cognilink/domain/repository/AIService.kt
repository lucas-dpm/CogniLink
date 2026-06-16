package com.lucasdpm.cognilink.domain.repository

import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.domain.model.FlashcardType

interface AIService {
    suspend fun generateFlashcards(
        theme: String,
        quantity: Int,
        difficulty: DifficultyLevel?,
        type: FlashcardType?
    ): Result<List<GeneratedFlashcard>>
}

data class GeneratedFlashcard(
    val question: String,
    val answer: String,
    val options: List<String>? = null
)
