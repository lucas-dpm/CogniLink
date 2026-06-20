package com.lucasdpm.cognilink.data.datebase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lucasdpm.cognilink.data.datebase.entities.FlashcardStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardStatsDao {
    @Query("SELECT * FROM flashcards_stats WHERE flashcardId = :flashcardId")
    fun getFlashcardStatsById(flashcardId: String): Flow<FlashcardStatsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcardStats(stats: FlashcardStatsEntity)

    @Update
    suspend fun updateFlashcardStats(stats: FlashcardStatsEntity)

    @Query("DELETE FROM flashcards_stats")
    suspend fun deleteAllFlashcardStats()

    @Query("DELETE FROM flashcards_stats WHERE flashcardId = :flashcardId")
    suspend fun deleteFlashcardStatsById(flashcardId: String)

    @Query("""
        SELECT fs.* FROM flashcards_stats fs
        INNER JOIN flashcards f ON fs.flashcardId = f.id
        INNER JOIN decks d ON f.deckId = d.id
        WHERE d.userId = :userId
    """)
    suspend fun getAllFlashcardStatsForUser(userId: String): List<FlashcardStatsEntity>
}
