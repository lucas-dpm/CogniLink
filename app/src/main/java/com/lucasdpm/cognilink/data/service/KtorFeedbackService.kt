package com.lucasdpm.cognilink.data.service

import com.lucasdpm.cognilink.domain.repository.FeedbackService
import io.ktor.client.HttpClient
import kotlinx.serialization.Serializable

class KtorFeedbackService(private val httpClient: HttpClient) : FeedbackService {

    override suspend fun getLlmFeedback(userAnswer: String, correctAnswer: String): String {
        return try {
            // Exemplo de chamada para uma API (substitua pela sua URL e chave)
            // val response = httpClient.post("https://api.seubackend.com/v1/feedback") {
            //     contentType(ContentType.Application.Json)
            //     setBody(FeedbackRequest(userAnswer, correctAnswer))
            // }
            // response.bodyAsText()
            
            "Feedback simulado da IA: Sua resposta está quase completa, mas faltou mencionar como o TFLite gerencia a memória."
        } catch (e: Exception) {
            throw e
        }
    }
}

@Serializable
data class FeedbackRequest(val userAnswer: String, val correctAnswer: String)

