package com.example.cognilink.data.repository

import com.example.cognilink.data.model.Flashcard
import com.example.cognilink.data.model.flashcard1
import com.example.cognilink.data.model.flashcard2
import com.example.cognilink.data.model.flashcard3
import com.example.cognilink.data.model.flashcard4

interface FlashcardRepository {
    suspend fun getFlashcardsForDeck(deckId: Long): List<Flashcard>
    suspend fun saveFlashcard(flashcard: Flashcard)
    suspend fun deleteFlashcard(flashcardId: String)
}

class FlashcardRepositoryImpl : FlashcardRepository {
    override suspend fun getFlashcardsForDeck(deckId: Long): List<Flashcard> {
        // Filtra os flashcards pelo deckId (simulação)
        return listOf(flashcard1, flashcard2, flashcard3, flashcard4).filter { it.deckId == deckId }
    }

    override suspend fun saveFlashcard(flashcard: Flashcard) {
        // Simulação de salvar flashcard
    }

    override suspend fun deleteFlashcard(flashcardId: String) {
        // Simulação de deletar flashcard
    }
}
