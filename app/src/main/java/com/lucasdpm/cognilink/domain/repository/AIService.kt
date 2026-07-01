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

    suspend fun startFeynmanChat(
        theme: String,
        sessionId: String? = null
    ): Result<FeynmanStartResponse>

    suspend fun sendFeynmanMessage(
        sessionId: String,
        message: String
    ): Result<FeynmanMessageResponse>
}

data class FeynmanChatMessage(
    val text: String,
    val isFromUser: Boolean
)

data class FeynmanStartResponse(
    val sessionId: String,
    val personaName: String,
    val initialMessage: String
)

data class FeynmanMessageResponse(
    val reply: String,
    val isFinished: Boolean,
    val sm2Quality: Int? = null
)

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
