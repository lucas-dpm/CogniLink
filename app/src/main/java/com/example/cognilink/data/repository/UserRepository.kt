package com.example.cognilink.data.repository

import com.example.cognilink.data.model.User
import com.example.cognilink.data.model.UserStats
import com.example.cognilink.data.model.fakeUser

interface UserRepository {
    suspend fun getUserById(userId: Long): User
    suspend fun updateUser(user: User)
}

class UserRepositoryImpl : UserRepository {
    override suspend fun getUserById(userId: Long): User {
        return fakeUser
    }

    override suspend fun updateUser(user: User) {
        // Simulação de atualização de usuário
    }
}
