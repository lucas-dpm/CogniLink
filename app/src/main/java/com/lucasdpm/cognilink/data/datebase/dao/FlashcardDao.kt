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

    @androidx.room.Transaction
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
}