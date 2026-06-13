package com.lucasdpm.cognilink.domain.usecase

import com.lucasdpm.cognilink.data.repository.FlashcardRepository
import kotlinx.coroutines.flow.Flow

class CalculateDeckReviewCountUseCase(
    private val repository: FlashcardRepository
) {
    // O operador invoke permite chamar a classe como se fosse uma função
    operator fun invoke(deckId: String): Flow<Int> {
        val todayTimestamp = System.currentTimeMillis() // Pega a data/hora atual
        return repository.getReviewCountForDeck(deckId, todayTimestamp)
    }
}