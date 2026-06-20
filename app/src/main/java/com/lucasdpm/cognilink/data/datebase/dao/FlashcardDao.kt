package com.lucasdpm.cognilink.data.datebase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.lucasdpm.cognilink.data.datebase.entities.FlashcardEntity
import com.lucasdpm.cognilink.data.datebase.entities.FlashcardWithStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcards")
    fun getAllFlashcards(): Flow<List<FlashcardEntity>>

    @Transaction
    @Query("SELECT * FROM flashcards WHERE id = :id")
    suspend fun getFlashcardById(id: String): FlashcardWithStatsEntity?

    @Transaction
    @Query("SELECT * FROM flashcards WHERE deckId = :id")
    fun getFlashcardForDeckById(id: String): Flow<List<FlashcardWithStatsEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFlashcard(flashcard: FlashcardEntity): Long

    @androidx.room.Update
    suspend fun updateFlashcard(flashcard: FlashcardEntity)

    @Transaction
    suspend fun upsertFlashcard(flashcard: FlashcardEntity) {
        val id = insertFlashcard(flashcard)
        if (id == -1L) {
            updateFlashcard(flashcard)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllFlashcards(flashcards: List<FlashcardEntity>)

    @Query("DELETE FROM flashcards")
    suspend fun deleteAllFlashcards()

    @Query("DELETE FROM flashcards WHERE id = :id")
    suspend fun deleteFlashcardById(id: String)

    @Transaction
    @Query("""
        SELECT f.* FROM flashcards f
        INNER JOIN decks d ON f.deckId = d.id
        WHERE d.userId = :userId AND f.id IN (
            SELECT flashcardId FROM flashcards_stats WHERE consecutiveMisses >= 4
        )
    """)
    suspend fun getLeeches(userId: String): List<FlashcardWithStatsEntity>

    @Transaction
    @Query("""
        SELECT f.* FROM flashcards f
        INNER JOIN decks d ON f.deckId = d.id
        LEFT JOIN flashcards_stats fs ON f.id = fs.flashcardId
        WHERE d.userId = :userId AND (fs.nextReview <= :currentTime OR fs.nextReview IS NULL)
    """)
    suspend fun getReviewPending(userId: String, currentTime: Long): List<FlashcardWithStatsEntity>
}