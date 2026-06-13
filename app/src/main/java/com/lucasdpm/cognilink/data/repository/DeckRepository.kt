package com.lucasdpm.cognilink.data.repository

import com.lucasdpm.cognilink.data.datebase.CogniLinkDatabase
import com.lucasdpm.cognilink.data.datebase.dao.DeckDao
import com.lucasdpm.cognilink.data.mappers.toDomain
import com.lucasdpm.cognilink.data.mappers.toEntity
import com.lucasdpm.cognilink.data.model.Deck
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface DeckRepository {
    fun getDecks(userId: String): Flow<List<Deck>>
    fun getDeckById(deckId: String, userId: String): Flow<Deck?>
    suspend fun saveDeck(deck: Deck, userId: String)
    suspend fun deleteDeck(deckId: String, userId: String)
}

class DeckRepositoryImpl(
    private val db: CogniLinkDatabase,
    private val deckDao: DeckDao
) : DeckRepository {
    
    override fun getDecks(userId: String): Flow<List<Deck>> {
        val currentTime = System.currentTimeMillis()
        return deckDao.getDecksWithStats(userId, currentTime).map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getDeckById(deckId: String, userId: String): Flow<Deck?> {
        val currentTime = System.currentTimeMillis()
        return deckDao.getDeckWithStatsById(deckId, currentTime).map { entity ->
            // Verificação de segurança: garante que o deck pertence ao usuário
            if (entity?.deck?.userId == userId) entity.toDomain() else null
        }
    }

    override suspend fun saveDeck(deck: Deck, userId: String) {
        // Garante que o userId está correto antes de salvar/atualizar
        deckDao.upsertDeck(deck.toEntity().copy(userId = userId))
    }

    override suspend fun deleteDeck(deckId: String, userId: String) {
        val entity = deckDao.getDeckById(deckId).first()
        if (entity?.userId == userId) {
            deckDao.deleteDeckById(deckId)
        }
    }
}
