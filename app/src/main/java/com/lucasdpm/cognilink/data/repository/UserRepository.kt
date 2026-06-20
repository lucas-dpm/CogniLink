package com.lucasdpm.cognilink.data.repository

import android.util.Log
import androidx.room.withTransaction
import com.lucasdpm.cognilink.data.datebase.CogniLinkDatabase
import com.lucasdpm.cognilink.data.datebase.dao.UserDao
import com.lucasdpm.cognilink.data.datebase.dao.UserStatsDao
import com.lucasdpm.cognilink.data.mappers.toDomain
import com.lucasdpm.cognilink.data.mappers.toEntity
import com.lucasdpm.cognilink.data.model.User
import com.lucasdpm.cognilink.data.model.UserStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface UserRepository {
    suspend fun getUserById(userId: String): User?
    fun getUserStats(userId: String): Flow<UserStats?>
    suspend fun updateUser(user: User)
}

class UserRepositoryImpl(
    private val db: CogniLinkDatabase,
    private val userDao: UserDao,
    private val userStatsDao: UserStatsDao
) : UserRepository {

    companion object {
        private const val TAG = "UserRepository"
    }

    override suspend fun getUserById(userId: String): User? {
        return try {
            val userEntity = userDao.findUserById(userId) ?: return null
            userEntity.toDomain(UserStats(userId = userId))
        } catch (e: Exception) {
            Log.e(TAG, "getUserById: Erro ao buscar usuário $userId", e)
            null
        }
    }

    override fun getUserStats(userId: String): Flow<UserStats?> {
        return userStatsDao.getUserStatsByUserId(userId).map { it?.toDomain() }
    }

    override suspend fun updateUser(user: User) {
        try {
            db.withTransaction {
                userDao.saveUser(user.toEntity())
                userStatsDao.saveUserStats(user.stats.toEntity())
            }
        } catch (e: Exception) {
            Log.e(TAG, "updateUser: Erro ao atualizar usuário ${user.id}", e)
            throw e
        }
    }
}
