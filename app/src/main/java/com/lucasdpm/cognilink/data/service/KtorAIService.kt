package com.lucasdpm.cognilink.data.service

import com.lucasdpm.cognilink.BuildConfig
import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.domain.model.FlashcardType
import com.lucasdpm.cognilink.domain.service.AIAnswerFeedback
import com.lucasdpm.cognilink.domain.service.AIService
import com.lucasdpm.cognilink.domain.service.GeneratedFlashcard
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class KtorAIService(
    private val httpClient: HttpClient,
) : AIService {

    private val baseUrl = BuildConfig.BASE_BACKEND_URL

    override suspend fun generateFlashcards(
        theme: String,
        quantity: Int,
        difficulty: DifficultyLevel?,
        type: FlashcardType?,
    ): Result<List<GeneratedFlashcard>> {
        return runCatching {
            val response = httpClient.post("${baseUrl}/ai/generate") {
                contentType(ContentType.Application.Json)
                setBody(GenerateRequest(theme, quantity, difficulty?.name, type?.name))
            }.body<List<GeneratedFlashcardDto>>()

            response.map {
                GeneratedFlashcard(
                    question = it.question,
                    answer = it.answer,
                    options = it.options
                )
            }
        }
    }

    override suspend fun compareAnswer(
        question: String,
        correctAnswer: String,
        userAnswer: String,
    ): Result<AIAnswerFeedback> {
        return runCatching {
            val response = httpClient.post("${baseUrl}/ai/compare-answer") {
                contentType(ContentType.Application.Json)
                setBody(CompareRequest(question, correctAnswer, userAnswer))
            }.body<CompareResponse>()

            AIAnswerFeedback(
                isCorrect = response.isCorrect,
                tip = response.tip
            )
        }
    }

    @Serializable
    private data class GenerateRequest(
        val theme: String,
        val quantity: Int,
        val difficulty: String?,
        val type: String?
    )

    @Serializable
    private data class GeneratedFlashcardDto(
        val question: String,
        val answer: String,
        val options: List<String>? = null
    )

    @Serializable
    private data class CompareRequest(
        val question: String,
        val correctAnswer: String,
        val userAnswer: String
    )

    @Serializable
    private data class CompareResponse(
        val isCorrect: Boolean,
        val tip: String
    )
}
