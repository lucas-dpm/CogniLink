package com.lucasdpm.cognilink.data.repository

import android.util.Log
import com.lucasdpm.cognilink.data.datebase.dao.StudyContextDao
import com.lucasdpm.cognilink.data.datebase.entities.DeckContextJoinEntity
import com.lucasdpm.cognilink.data.mappers.toDomain
import com.lucasdpm.cognilink.data.mappers.toEntity
import com.lucasdpm.cognilink.data.model.Deck
import com.lucasdpm.cognilink.data.model.StudyContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface StudyContextRepository {
    fun getContextsForUser(userId: String): Flow<List<StudyContext>>
    suspend fun saveContext(context: StudyContext)
    suspend fun deleteContext(context: StudyContext)
    suspend fun getContextById(id: String): StudyContext?
    
    // Vinculação muitos-para-muitos
    suspend fun linkDeckToContext(deckId: String, contextId: String)
    suspend fun unlinkDeckFromContext(deckId: String, contextId: String)
    fun getDecksForContext(contextId: String): Flow<List<Deck>>
    fun getContextsForDeck(deckId: String): Flow<List<StudyContext>>
}

class StudyContextRepositoryImpl(
    private val studyContextDao: StudyContextDao
) : StudyContextRepository {

    companion object {
        private const val TAG = "StudyContextRepository"
    }

    override fun getContextsForUser(userId: String): Flow<List<StudyContext>> {
        return studyContextDao.getContextsForUser(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun saveContext(context: StudyContext) {
        try {
            studyContextDao.insertContext(context.toEntity())
        } catch (e: Exception) {
            Log.e(TAG, "saveContext: Erro ao salvar contexto", e)
            throw e
        }
    }

    override suspend fun deleteContext(context: StudyContext) {
        try {
            studyContextDao.deleteContext(context.toEntity())
        } catch (e: Exception) {
            Log.e(TAG, "deleteContext: Erro ao deletar contexto", e)
            throw e
        }
    }

    override suspend fun getContextById(id: String): StudyContext? {
        return studyContextDao.getContextById(id)?.toDomain()
    }

    override suspend fun linkDeckToContext(deckId: String, contextId: String) {
        try {
            studyContextDao.insertDeckContextJoin(DeckContextJoinEntity(deckId, contextId))
        } catch (e: Exception) {
            Log.e(TAG, "linkDeckToContext: Erro ao vincular deck ao contexto", e)
            throw e
        }
    }

    override suspend fun unlinkDeckFromContext(deckId: String, contextId: String) {
        try {
            studyContextDao.deleteDeckContextJoin(DeckContextJoinEntity(deckId, contextId))
        } catch (e: Exception) {
            Log.e(TAG, "unlinkDeckFromContext: Erro ao desvincular deck do contexto", e)
            throw e
        }
    }

    override fun getDecksForContext(contextId: String): Flow<List<Deck>> {
        return studyContextDao.getDecksForContext(contextId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getContextsForDeck(deckId: String): Flow<List<StudyContext>> {
        return studyContextDao.getContextsForDeck(deckId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
