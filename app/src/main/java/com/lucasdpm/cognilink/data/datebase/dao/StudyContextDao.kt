package com.lucasdpm.cognilink.data.datebase.dao

import androidx.room.*
import com.lucasdpm.cognilink.data.datebase.entities.DeckContextJoinEntity
import com.lucasdpm.cognilink.data.datebase.entities.DeckEntity
import com.lucasdpm.cognilink.data.datebase.entities.StudyContextEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyContextDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContext(context: StudyContextEntity)

    @Update
    suspend fun updateContext(context: StudyContextEntity)

    @Delete
    suspend fun deleteContext(context: StudyContextEntity)

    @Query("SELECT * FROM study_contexts WHERE userId = :userId")
    fun getContextsForUser(userId: String): Flow<List<StudyContextEntity>>

    @Query("SELECT * FROM study_contexts WHERE id = :contextId")
    suspend fun getContextById(contextId: String): StudyContextEntity?

    // Operações de junção muitos-para-muitos
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDeckContextJoin(join: DeckContextJoinEntity)

    @Delete
    suspend fun deleteDeckContextJoin(join: DeckContextJoinEntity)

    @Query("""
        SELECT d.* FROM decks d
        INNER JOIN deck_context_join dcj ON d.id = dcj.deckId
        WHERE dcj.contextId = :contextId
    """)
    fun getDecksForContext(contextId: String): Flow<List<DeckEntity>>

    @Query("""
        SELECT sc.* FROM study_contexts sc
        INNER JOIN deck_context_join dcj ON sc.id = dcj.contextId
        WHERE dcj.deckId = :deckId
    """)
    fun getContextsForDeck(deckId: String): Flow<List<StudyContextEntity>>
}
