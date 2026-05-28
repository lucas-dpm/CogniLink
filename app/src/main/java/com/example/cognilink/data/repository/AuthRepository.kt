package com.example.cognilink.data.repository

import com.example.cognilink.data.model.User
import com.example.cognilink.data.model.fakeUser

interface AuthRepository {
    suspend fun signIn(email: String, password: String): User?
    suspend fun signUp(name: String, email: String, password: String): User?
}

class AuthRepositoryImpl : AuthRepository {
    override suspend fun signIn(email: String, password: String): User? {
        // Simulação de login
        return if (email == "alex.silva@example.com" && password == "123456") {
            fakeUser
        } else {
            null
        }
    }

    override suspend fun signUp(name: String, email: String, password: String): User? {
        // Simulação de registro
        return fakeUser.copy(name = name, email = email)
    }
}
