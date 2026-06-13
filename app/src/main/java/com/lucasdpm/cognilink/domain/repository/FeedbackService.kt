package com.lucasdpm.cognilink.domain.repository

interface FeedbackService {
    suspend fun getLlmFeedback(userAnswer: String, correctAnswer: String): String
}
