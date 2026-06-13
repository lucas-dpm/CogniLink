package com.lucasdpm.cognilink.data.datebase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lucasdpm.cognilink.data.datebase.entities.DeckEntity
import com.lucasdpm.cognilink.data.datebase.entities.DeckWithStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {
    @Query("SELECT * FROM decks")
    fun getAllDecks(): Flow<List<DeckEntity>>

    @Query("""
        SELECT 
            d.*, 
            (SELECT COUNT(*) FROM flashcards f WHERE f.deckId = d.id) as totalCards,
            (SELECT COUNT(*) FROM flashcards f 
             LEFT JOIN flashcards_stats s ON f.id = s.flashcardId 
             WHERE f.deckId = d.id AND (s.nextReview <= :currentTime OR s.nextReview IS NULL)) as cardsToReview,
            (SELECT COALESCE(AVG(CASE f.difficulty WHEN 'EASY' THEN 1 WHEN 'MEDIUM' THEN 2 WHEN 'HARD' THEN 3 ELSE 1 END), 1.0) 
             FROM flashcards f WHERE f.deckId = d.id) as averageDifficulty,
            (SELECT COALESCE(AVG(s.mastery), 0.0) 
             FROM flashcards f 
             INNER JOIN flashcards_stats s ON f.id = s.flashcardId 
             WHERE f.deckId = d.id) as averageMastery
        FROM decks d
        WHERE d.userId = :userId
    """)
    fun getDecksWithStats(userId: String, currentTime: Long): Flow<List<DeckWithStatsEntity>>

    @Query("""
        SELECT 
            d.*, 
            (SELECT COUNT(*) FROM flashcards f WHERE f.deckId = d.id) as totalCards,
            (SELECT COUNT(*) FROM flashcards f 
             LEFT JOIN flashcards_stats s ON f.id = s.flashcardId 
             WHERE f.deckId = d.id AND (s.nextReview <= :currentTime OR s.nextReview IS NULL)) as cardsToReview,
            (SELECT COALESCE(AVG(CASE f.difficulty WHEN 'EASY' THEN 1 WHEN 'MEDIUM' THEN 2 WHEN 'HARD' THEN 3 ELSE 1 END), 1.0) 
             FROM flashcards f WHERE f.deckId = d.id) as averageDifficulty,
            (SELECT COALESCE(AVG(s.mastery), 0.0) 
             FROM flashcards f 
             INNER JOIN flashcards_stats s ON f.id = s.flashcardId 
             WHERE f.deckId = d.id) as averageMastery
        FROM decks d
        WHERE d.id = :id
    """)
    fun getDeckWithStatsById(id: String, currentTime: Long): Flow<DeckWithStatsEntity?>

    @Query("SELECT * FROM decks WHERE userId = :userId")
    fun getDecksByUserId(userId: String): Flow<List<DeckEntity>>

    @Query("SELECT * FROM decks WHERE id = :id")
    fun getDeckById(id: String): Flow<DeckEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDeck(deck: DeckEntity): Long

    @androidx.room.Update
    suspend fun updateDeck(deck: DeckEntity)

    @androidx.room.Transaction
    suspend fun upsertDeck(deck: DeckEntity) {
        val id = insertDeck(deck)
        if (id == -1L) {
            updateDeck(deck)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllDecks(decks: List<DeckEntity>)

    @Query("DELETE FROM decks")
    suspend fun deleteAllDecks()

    @Query("DELETE FROM decks WHERE id = :id")
    suspend fun deleteDeckById(id: String)
}