package com.lucasdpm.cognilink.data.repository

import com.lucasdpm.cognilink.data.datebase.dao.DeckDao
import com.lucasdpm.cognilink.data.datebase.dao.FlashcardDao
import com.lucasdpm.cognilink.data.datebase.dao.FlashcardStatsDao
import com.lucasdpm.cognilink.data.mappers.toDomain
import com.lucasdpm.cognilink.data.mappers.toEntity
import com.lucasdpm.cognilink.data.model.Flashcard
import com.lucasdpm.cognilink.data.model.FlashcardStats
import com.lucasdpm.cognilink.data.model.FlashcardWithStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

interface FlashcardRepository {
    fun getFlashcardsForDeck(deckId: String): Flow<List<FlashcardWithStats>>
    suspend fun saveFlashcard(flashcard: Flashcard)
    suspend fun deleteFlashcard(flashcardId: String)
    suspend fun getFlashcardById(flashcardId: String): FlashcardWithStats?
    suspend fun saveAllFlashcards(flashcards: List<Flashcard>)
    fun getFlashcardStatistics(flashcardId: String): Flow<FlashcardStats?>
    suspend fun getLeeches(userId: String): List<FlashcardWithStats>?
    suspend fun getReviewPending(userId: String): List<FlashcardWithStats>?
    suspend fun getDeckName(deckId: String): String?
    fun getReviewCountForDeck(deckId: String, todayTimestamp: Long): Flow<Int>
    suspend fun updateFlashcardStatistics(statistics: FlashcardStats)
    suspend fun getFlashcardsToReview(deckId: String, currentTimestamp: Long): List<FlashcardWithStats>
    suspend fun resetStatistics(flashcardId: String)
}

class FlashcardRepositoryImpl(
    private val flashcardDao: FlashcardDao,
    private val flashcardStatsDao: FlashcardStatsDao,
    private val deckDao: DeckDao
) : FlashcardRepository {
    override fun getFlashcardsForDeck(deckId: String): Flow<List<FlashcardWithStats>> {
        return flashcardDao.getFlashcardForDeckById(deckId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun saveFlashcard(flashcard: Flashcard) {
        flashcardDao.upsertFlashcard(flashcard.toEntity())
    }

    override suspend fun deleteFlashcard(flashcardId: String) {
        flashcardDao.deleteFlashcardById(flashcardId)
    }

    override suspend fun getFlashcardById(flashcardId: String): FlashcardWithStats? {
        return flashcardDao.getFlashcardById(flashcardId)?.toDomain()
    }

    override suspend fun saveAllFlashcards(flashcards: List<Flashcard>) {
        flashcardDao.saveAllFlashcards(flashcards.map { it.toEntity() })
    }

    override fun getFlashcardStatistics(flashcardId: String): Flow<FlashcardStats?> {
        return flashcardStatsDao.getFlashcardStatsById(flashcardId).map { it?.toDomain() }
    }

    override suspend fun getLeeches(userId: String): List<FlashcardWithStats>? {
        // TODO:
        // Implementar lógica de busca de leeches baseada nas estatísticas
        // Por agora retornamos vazio ou implementamos se houver query no DAO
        return null
    }

    override suspend fun getReviewPending(userId: String): List<FlashcardWithStats>? {
        // TODO:
        // Implementar lógica de cards pendentes
        return null
    }

    override suspend fun getDeckName(deckId: String): String? {
        return deckDao.getDeckById(deckId).first()?.name
    }

    override fun getReviewCountForDeck(deckId: String, todayTimestamp: Long): Flow<Int> {
        return deckDao.getDeckWithStatsById(deckId, todayTimestamp).map { it?.cardsToReview ?: 0 }
    }

    override suspend fun updateFlashcardStatistics(statistics: FlashcardStats) {
        flashcardStatsDao.insertFlashcardStats(statistics.toEntity())
    }

    override suspend fun getFlashcardsToReview(deckId: String, currentTimestamp: Long): List<FlashcardWithStats> {
        return flashcardDao.getFlashcardForDeckById(deckId).first().filter { 
            it.stats?.nextReview == null || it.stats.nextReview <= currentTimestamp
        }.map { it.toDomain() }
    }

    override suspend fun resetStatistics(flashcardId: String) {
        flashcardStatsDao.deleteFlashcardStatsById(flashcardId)
    }
}
