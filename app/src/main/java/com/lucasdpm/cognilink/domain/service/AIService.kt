package com.lucasdpm.cognilink.domain.service

import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.domain.model.FlashcardType

interface AIService {
    suspend fun generateFlashcards(
        theme: String,
        quantity: Int,
        difficulty: DifficultyLevel?,
        type: FlashcardType?
    ): Result<List<GeneratedFlashcard>>

    suspend fun compareAnswer(
        question: String,
        correctAnswer: String,
        userAnswer: String
    ): Result<AIAnswerFeedback>
}

data class GeneratedFlashcard(
    val question: String,
    val answer: String,
    val options: List<String>? = null
)

data class AIAnswerFeedback(
    val isCorrect: Boolean,
    val tip: String
)
