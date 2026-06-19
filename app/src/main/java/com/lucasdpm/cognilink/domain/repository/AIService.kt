package com.lucasdpm.cognilink.domain.repository

import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.domain.model.FlashcardType

interface AIService {
    suspend fun compareAnswer(
        question: String,
        correctAnswer: String,
        userAnswer: String
    ): Result<AIAnswerFeedback>

    suspend fun analyzeDocument(
        fileBytes: ByteArray,
        fileName: String
    ): Result<DocumentAnalysis>

    suspend fun generateFlashcardsWithIA(
        mainTheme: String,
        topics: List<String>,
        difficulty: String,
        type: String,
        quantity: Int
    ): Result<List<IAGeneratedFlashcard>>
}

data class DocumentAnalysis(
    val mainTheme: String,
    val topics: List<String>
)

data class IAGeneratedFlashcard(
    val question: String,
    val type: FlashcardType,
    val difficulty: DifficultyLevel,
    val hints: List<String>,
    val answerOptions: List<IAGeneratedAnswer>
)

data class IAGeneratedAnswer(
    val answer: String,
    val isCorrect: Boolean
)

data class AIAnswerFeedback(
    val isCorrect: Boolean,
    val tip: String
)
