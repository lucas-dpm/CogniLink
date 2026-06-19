package com.lucasdpm.cognilink.data.service

import com.lucasdpm.cognilink.BuildConfig
import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.domain.model.FlashcardType
import com.lucasdpm.cognilink.domain.service.AIAnswerFeedback
import com.lucasdpm.cognilink.domain.service.AIService
import com.lucasdpm.cognilink.domain.service.DocumentAnalysis
import com.lucasdpm.cognilink.domain.service.IAGeneratedAnswer
import com.lucasdpm.cognilink.domain.service.IAGeneratedFlashcard
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class KtorAIService(
    private val httpClient: HttpClient,
) : AIService {

    private val baseUrl = BuildConfig.BASE_BACKEND_URL

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

    override suspend fun analyzeDocument(
        fileBytes: ByteArray,
        fileName: String
    ): Result<DocumentAnalysis> {
        return runCatching {
            val response = httpClient.submitFormWithBinaryData(
                url = "${baseUrl}/ai/analyze-document",
                formData = formData {
                    append("file", fileBytes, Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=$fileName")
                    })
                }
            ).body<AnalyzeDocumentResponse>()

            DocumentAnalysis(
                mainTheme = response.mainTheme,
                topics = response.topics
            )
        }
    }

    override suspend fun generateFlashcardsWithIA(
        topics: List<String>,
        difficulty: String,
        type: String,
        quantity: Int
    ): Result<List<IAGeneratedFlashcard>> {
        return runCatching {
            val response = httpClient.post("${baseUrl}/ai/generate-flashcards") {
                contentType(ContentType.Application.Json)
                setBody(IAGenerationRequest(topics, difficulty, type, quantity))
            }.body<IAGenerationResponse>()

            response.flashcards.map { dto ->
                IAGeneratedFlashcard(
                    question = dto.question,
                    type = FlashcardType.valueOf(dto.type.uppercase()),
                    difficulty = DifficultyLevel.valueOf(dto.difficulty.uppercase()),
                    hints = dto.hints,
                    answerOptions = dto.answerOptions.map { opt ->
                        IAGeneratedAnswer(
                            answer = opt.answerText,
                            isCorrect = opt.isCorrect
                        )
                    }
                )
            }
        }
    }

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

    @Serializable
    private data class AnalyzeDocumentResponse(
        @SerialName("main-theme")
        val mainTheme: String,
        val topics: List<String>
    )

    @Serializable
    private data class IAGenerationRequest(
        val topics: List<String>,
        val difficulty: String,
        val type: String,
        val quantity: Int
    )

    @Serializable
    private data class IAGenerationResponse(
        val flashcards: List<IAGeneratedFlashcardDto>
    )

    @Serializable
    private data class IAGeneratedFlashcardDto(
        val question: String,
        val type: String,
        val difficulty: String,
        val hints: List<String>,
        val answerOptions: List<IAAnswerOptionDto>
    )

    @Serializable
    private data class IAAnswerOptionDto(
        val answerText: String,
        val isCorrect: Boolean
    )
}
