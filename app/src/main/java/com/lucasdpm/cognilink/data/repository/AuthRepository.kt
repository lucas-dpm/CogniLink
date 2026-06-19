package com.lucasdpm.cognilink.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.lucasdpm.cognilink.data.model.User
import com.lucasdpm.cognilink.data.model.UserStats
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull

interface AuthRepository {
    suspend fun signIn(email: String, password: String): User?
    suspend fun signUp(name: String, email: String, password: String): User?
}

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): User? {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return null
            
            // Tenta buscar dados adicionais no Firestore com timeout para evitar travamentos
            val userDoc = withTimeoutOrNull(5000) {
                firestore.collection("users").document(firebaseUser.uid).get().await()
            }
            
            val name = userDoc?.getString("name") ?: ""
            
            // Busca estatísticas
            val statsDoc = withTimeoutOrNull(5000) {
                firestore.collection("users").document(firebaseUser.uid)
                    .collection("stats").document("global").get().await()
            }
            
            val stats = if (statsDoc != null && statsDoc.exists()) {
                UserStats(
                    userId = firebaseUser.uid,
                    totalFlashcardsDone = statsDoc.getLong("totalFlashcardsDone")?.toInt() ?: 0,
                    totalFlashcardsMisses = statsDoc.getLong("totalFlashcardsMisses")?.toInt() ?: 0,
                    totalFlashcardsHits = statsDoc.getLong("totalFlashcardsHits")?.toInt() ?: 0,
                    lastReview = statsDoc.getLong("lastReview") ?: 0L,
                    totalStudyTime = statsDoc.getLong("totalStudyTime") ?: 0L,
                    totalFlashcardsReviewed = statsDoc.getLong("totalFlashcardsReviewed")?.toInt() ?: 0,
                    overallMastery = statsDoc.getDouble("overallMastery")?.toFloat() ?: 0f,
                    retentionRate = statsDoc.getDouble("retentionRate")?.toFloat() ?: 0f
                )
            } else {
                UserStats(userId = firebaseUser.uid)
            }

            val user = User(
                id = firebaseUser.uid,
                name = name,
                email = firebaseUser.email ?: "",
                stats = stats
            )

            // Salva no banco local (Room) para garantir o Offline-First
            userRepository.updateUser(user)

            user
        } catch (e: FirebaseAuthException) {
            Log.e("AuthRepository", "Firebase Auth Error: ${e.errorCode}")
            null
        } catch (e: Exception) {
            Log.e("AuthRepository", "Unknown Auth Error")
            null
        }
    }

    override suspend fun signUp(name: String, email: String, password: String): User? {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return null

            val newUser = User(
                id = firebaseUser.uid,
                name = name,
                email = email,
                stats = UserStats(userId = firebaseUser.uid)
            )

            // Salva dados no Firestore de forma resiliente
            try {
                withTimeoutOrNull(5000) {
                    val userMap = mapOf(
                        "name" to name,
                        "email" to email
                    )
                    firestore.collection("users").document(firebaseUser.uid).set(userMap).await()
                    
                    firestore.collection("users").document(firebaseUser.uid)
                        .collection("stats").document("global").set(newUser.stats).await()
                }
            } catch (firestoreError: Exception) {
                Log.e("AuthRepository", "Firestore Sync Error")
            }

            // Salva no banco local (Room) para prioridade de uso offline
            userRepository.updateUser(newUser)

            newUser
        } catch (e: FirebaseAuthException) {
            Log.e("AuthRepository", "Firebase Auth Error: ${e.errorCode}")
            null
        } catch (e: Exception) {
            Log.e("AuthRepository", "Unknown Auth Error")
            null
        }
    }
}
