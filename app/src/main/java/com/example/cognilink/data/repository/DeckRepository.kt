package com.example.cognilink.data.repository

import com.example.cognilink.data.model.Deck
import com.example.cognilink.data.model.deck1
import com.example.cognilink.data.model.deck2
import com.example.cognilink.data.model.deck3

interface DeckRepository {
    suspend fun getDecks(userId: Long): List<Deck>
    suspend fun getDeckById(deckId: Long, userId: Long): Deck?
    suspend fun saveDeck(deck: Deck, userId: Long)
    suspend fun deleteDeck(deckId: Long, userId: Long)
}

class DeckRepositoryImpl : DeckRepository {
    override suspend fun getDecks(userId: Long): List<Deck> {
        // Filtra os decks pelo userId (simulação)
        println("Buscando decks para o usuário: $userId")
        return listOf(deck1, deck2, deck3).filter { it.userId == userId }
    }

    override suspend fun getDeckById(deckId: Long, userId: Long): Deck? {
        return listOf(deck1, deck2, deck3).find { it.userId == userId && it.id == deckId }
    }

    override suspend fun saveDeck(deck: Deck, userId: Long) {
        // Simulação de salvar deck: Em um cenário real, isso persistiria no DB ou API
        println("Deck ${deck.name} salvo com sucesso para o usuário: $userId")
    }

    override suspend fun deleteDeck(deckId: Long, userId: Long) {
        // Simulação de deletar deck
        println("Deck com ID $deckId deletado para o usuário: $userId")
    }
}
