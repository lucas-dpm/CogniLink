package com.lucasdpm.cognilink.data.service

import com.lucasdpm.cognilink.BuildConfig
import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import com.lucasdpm.cognilink.domain.model.FlashcardType
import com.lucasdpm.cognilink.domain.repository.AIService
import com.lucasdpm.cognilink.domain.repository.GeneratedFlashcard
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class KtorAIService(private val httpClient: HttpClient) : AIService {

    private val baseUrl = BuildConfig.BASE_BACKEND_URL // Substitua pela URL do seu backend

    override suspend fun generateFlashcards(
        theme: String,
        quantity: Int,
        difficulty: DifficultyLevel?,
        type: FlashcardType?
    ): Result<List<GeneratedFlashcard>> {
        return try {
            val response = httpClient.post("$baseUrl/generate") {
                contentType(ContentType.Application.Json)
                setBody(
                    AIRequest(
                        theme = theme,
                        quantity = quantity,
                        difficulty = difficulty?.name,
                        type = type?.name
                    )
                )
            }
            
            val apiResponse = response.body<AIResponse>()
            Result.success(apiResponse.flashcards.map { 
                GeneratedFlashcard(it.question, it.answer, it.options)
            })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

@Serializable
data class AIRequest(
    val theme: String,
    val quantity: Int,
    val difficulty: String?,
    val type: String?
)

@Serializable
data class AIResponse(
    val flashcards: List<GeneratedFlashcardDTO>
)

@Serializable
data class GeneratedFlashcardDTO(
    val question: String,
    val answer: String,
    val options: List<String>? = null
)
