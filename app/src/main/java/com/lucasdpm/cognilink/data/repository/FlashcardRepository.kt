package com.lucasdpm.cognilink.data.repository

import android.util.Log
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
    suspend fun getAllStatisticsForUser(userId: String): List<FlashcardStats>
}

class FlashcardRepositoryImpl(
    private val flashcardDao: FlashcardDao,
    private val flashcardStatsDao: FlashcardStatsDao,
    private val deckDao: DeckDao
) : FlashcardRepository {

    companion object {
        private const val TAG = "FlashcardRepository"
    }

    override fun getFlashcardsForDeck(deckId: String): Flow<List<FlashcardWithStats>> {
        return flashcardDao.getFlashcardForDeckById(deckId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun saveFlashcard(flashcard: Flashcard) {
        try {
            flashcardDao.upsertFlashcard(flashcard.toEntity())
        } catch (e: Exception) {
            Log.e(TAG, "saveFlashcard: Erro ao salvar flashcard", e)
            throw e
        }
    }

    override suspend fun deleteFlashcard(flashcardId: String) {
        try {
            flashcardDao.deleteFlashcardById(flashcardId)
        } catch (e: Exception) {
            Log.e(TAG, "deleteFlashcard: Erro ao excluir flashcard", e)
            throw e
        }
    }

    override suspend fun getFlashcardById(flashcardId: String): FlashcardWithStats? {
        return try {
            flashcardDao.getFlashcardById(flashcardId)?.toDomain()
        } catch (e: Exception) {
            Log.e(TAG, "getFlashcardById: Erro ao buscar flashcard $flashcardId", e)
            null
        }
    }

    override suspend fun saveAllFlashcards(flashcards: List<Flashcard>) {
        try {
            flashcardDao.saveAllFlashcards(flashcards.map { it.toEntity() })
        } catch (e: Exception) {
            Log.e(TAG, "saveAllFlashcards: Erro ao salvar lista de flashcards", e)
            throw e
        }
    }

    override fun getFlashcardStatistics(flashcardId: String): Flow<FlashcardStats?> {
        return flashcardStatsDao.getFlashcardStatsById(flashcardId).map { it?.toDomain() }
    }

    override suspend fun getLeeches(userId: String): List<FlashcardWithStats> {
        return flashcardDao.getLeeches(userId).map { it.toDomain() }
    }

    override suspend fun getReviewPending(userId: String): List<FlashcardWithStats> {
        return flashcardDao.getReviewPending(userId, System.currentTimeMillis()).map { it.toDomain() }
    }

    override suspend fun getDeckName(deckId: String): String? {
        return deckDao.getDeckById(deckId).first()?.name
    }

    override fun getReviewCountForDeck(deckId: String, todayTimestamp: Long): Flow<Int> {
        return deckDao.getDeckWithStatsById(deckId, todayTimestamp).map { it?.cardsToReview ?: 0 }
    }

    override suspend fun updateFlashcardStatistics(statistics: FlashcardStats) {
        try {
            flashcardStatsDao.insertFlashcardStats(statistics.toEntity())
        } catch (e: Exception) {
            Log.e(TAG, "updateFlashcardStatistics: Erro ao atualizar estatísticas", e)
            throw e
        }
    }

    override suspend fun getFlashcardsToReview(deckId: String, currentTimestamp: Long): List<FlashcardWithStats> {
        return flashcardDao.getFlashcardForDeckById(deckId).first().filter { 
            it.stats?.nextReview == null || it.stats.nextReview <= currentTimestamp
        }.map { it.toDomain() }
    }

    override suspend fun resetStatistics(flashcardId: String) {
        flashcardStatsDao.deleteFlashcardStatsById(flashcardId)
    }

    override suspend fun getAllStatisticsForUser(userId: String): List<FlashcardStats> {
        return flashcardStatsDao.getAllFlashcardStatsForUser(userId).map { it.toDomain() }
    }
}
