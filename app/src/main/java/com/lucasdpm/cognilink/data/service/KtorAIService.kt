package com.lucasdpm.cognilink.data.service

import android.util.Log
import com.lucasdpm.cognilink.BuildConfig
import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.domain.model.FlashcardType
import com.lucasdpm.cognilink.domain.repository.AIAnswerFeedback
import com.lucasdpm.cognilink.domain.repository.AIService
import com.lucasdpm.cognilink.domain.repository.DocumentAnalysis
import com.lucasdpm.cognilink.domain.repository.FeynmanMessageResponse
import com.lucasdpm.cognilink.domain.repository.FeynmanStartResponse
import com.lucasdpm.cognilink.domain.repository.IAGeneratedAnswer
import com.lucasdpm.cognilink.domain.repository.IAGeneratedFlashcard
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.Serializable

class KtorAIService(
    private val httpClient: HttpClient,
) : AIService {

    companion object {
        private const val TAG = "KtorAIService"
    }

    private val baseUrl = BuildConfig.BASE_BACKEND_URL

    private suspend fun HttpResponse.ensureSuccess(errorMessage: String) {
        if (!status.isSuccess()) {
            val errorBody = bodyAsText()
            throw Exception("$errorMessage (${status.value}): $errorBody")
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
            }

            response.ensureSuccess("Erro ao comparar resposta")

            val body = response.body<CompareResponse>()

            AIAnswerFeedback(
                isCorrect = body.isCorrect,
                tip = body.tip
            )
        }.onFailure { e ->
            Log.e(TAG, "compareAnswer: Erro ao comparar resposta", e)
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
            )

            response.ensureSuccess("Erro ao analisar documento")

            val body = response.body<AnalyzeDocumentResponse>()

            DocumentAnalysis(
                mainTheme = body.mainTheme,
                topics = body.topics
            )
        }.onFailure { e ->
            Log.e(TAG, "analyzeDocument: Erro ao analisar documento", e)
        }
    }

    override suspend fun generateFlashcardsWithIA(
        mainTheme: String,
        topics: List<String>,
        difficulty: String,
        type: String,
        quantity: Int
    ): Result<List<IAGeneratedFlashcard>> {
        return runCatching {
            val response = httpClient.post("${baseUrl}/ai/generate-flashcards") {
                contentType(ContentType.Application.Json)
                setBody(GenerateFlashcardsRequest(mainTheme, topics, difficulty, type, quantity))
            }

            response.ensureSuccess("Erro ao gerar flashcards")

            val body = response.body<IAGenerationResponse>()

            body.flashcards.map { dto ->
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
        }.onFailure { e ->
            Log.e(TAG, "generateFlashcardsWithIA: Erro ao gerar flashcards", e)
        }
    }

    override suspend fun startFeynmanChat(
        theme: String,
        sessionId: String?
    ): Result<FeynmanStartResponse> {
        return runCatching {
            val response = httpClient.post("${baseUrl}/ai/feynman/start") {
                contentType(ContentType.Application.Json)
                setBody(FeynmanStartRequest(theme, sessionId))
            }

            response.ensureSuccess("Erro ao iniciar chat de Feynman")

            val body = response.body<KtorFeynmanStartResponse>()

            FeynmanStartResponse(
                sessionId = body.sessionId,
                personaName = body.personaName,
                initialMessage = body.initialMessage
            )
        }.onFailure { e ->
            Log.e(TAG, "startFeynmanChat: Erro ao iniciar chat", e)
        }
    }

    override suspend fun sendFeynmanMessage(
        sessionId: String,
        message: String
    ): Result<FeynmanMessageResponse> {
        return runCatching {
            val response = httpClient.post("${baseUrl}/ai/feynman/message") {
                contentType(ContentType.Application.Json)
                setBody(FeynmanMessageRequest(message, sessionId))
            }

            response.ensureSuccess("Erro ao enviar mensagem no chat de Feynman")

            val body = response.body<KtorFeynmanMessageResponse>()

            FeynmanMessageResponse(
                reply = body.reply,
                isFinished = body.isFinished,
                sm2Quality = body.sm2Quality
            )
        }.onFailure { e ->
            Log.e(TAG, "sendFeynmanMessage: Erro ao enviar mensagem", e)
        }
    }

    override suspend fun closeFeynmanChat(sessionId: String): Result<Unit> {
        return runCatching {
            Log.d(TAG, "closeFeynmanChat: Closing session $sessionId")
            val response = httpClient.post("${baseUrl}/ai/feynman/close/$sessionId")
            
            if (response.status.value == 404) {
                Log.w(TAG, "closeFeynmanChat: Session $sessionId not found (404). It might be already closed.")
                return@runCatching
            }

            response.ensureSuccess("Erro ao fechar chat de Feynman")
        }.onFailure { e ->
            Log.e(TAG, "closeFeynmanChat: Erro ao fechar chat", e)
        }
    }

    @Serializable
    private data class FeynmanStartRequest(
        val theme: String,
        val sessionId: String?
    )

    @Serializable
    private data class KtorFeynmanStartResponse(
        val sessionId: String,
        val personaName: String,
        val initialMessage: String
    )

    @Serializable
    private data class FeynmanMessageRequest(
        val message: String,
        val sessionId: String
    )

    @Serializable
    private data class KtorFeynmanMessageResponse(
        val reply: String,
        val isFinished: Boolean,
        val sm2Quality: Int? = null
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

    @Serializable
    private data class AnalyzeDocumentResponse(
        val mainTheme: String,
        val topics: List<String>
    )

    @Serializable
    private data class GenerateFlashcardsRequest(
        val mainTheme: String? = null,
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
