package com.lucasdpm.cognilink.data.repository

import androidx.room.withTransaction
import com.lucasdpm.cognilink.data.datebase.CogniLinkDatabase
import com.lucasdpm.cognilink.data.datebase.dao.UserDao
import com.lucasdpm.cognilink.data.datebase.dao.UserStatsDao
import com.lucasdpm.cognilink.data.mappers.toDomain
import com.lucasdpm.cognilink.data.mappers.toEntity
import com.lucasdpm.cognilink.data.model.User
import com.lucasdpm.cognilink.data.model.UserStats
import kotlinx.coroutines.flow.first
import java.util.UUID

interface AuthRepository {
    suspend fun signIn(email: String): User?
    suspend fun signUp(name: String, email: String): User?
}

class AuthRepositoryImpl(
    private val db: CogniLinkDatabase,
    private val userDao: UserDao,
    private val userStatsDao: UserStatsDao
) : AuthRepository {

    override suspend fun signIn(email: String): User? {
        // Busca o usuário pelo e-mail
        val userEntity = userDao.findUserByEmail(email) ?: return null
        
        // Num sistema real com Firebase, a senha seria validada pelo Firebase.
        // Aqui buscamos as estatísticas para montar o objeto de domínio completo.
        val statsEntity = userStatsDao.getUserStatsByUserId(userEntity.id).first()
        val stats = statsEntity?.toDomain() ?: UserStats(userId = userEntity.id)
        
        return userEntity.toDomain(stats)
    }

    override suspend fun signUp(name: String, email: String): User? {
        // Verifica se o usuário já existe
        if (userDao.findUserByEmail(email) != null) return null

        // Cria um ID único (Simulando o UID do Firebase)
        val newUserId = UUID.randomUUID().toString()

        val newUser = User(
            id = newUserId,
            name = name,
            email = email,
            stats = UserStats(userId = newUserId)
        )

        // Salva no Room em uma transação
        db.withTransaction {
            userDao.saveUser(newUser.toEntity())
            userStatsDao.insertUserStats(newUser.stats.toEntity())
        }

        return newUser
    }

}
