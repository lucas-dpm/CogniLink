package com.lucasdpm.cognilink.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.lucasdpm.cognilink.data.model.User
import com.lucasdpm.cognilink.data.model.UserStats
import com.lucasdpm.cognilink.domain.repository.NetworkMonitor
import com.lucasdpm.cognilink.domain.service.AppNotificationService
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeoutOrNull

interface AuthRepository {
    suspend fun signIn(email: String, password: String): User?
    suspend fun signUp(name: String, email: String, password: String): User?
    suspend fun signOut()
    suspend fun changePassword(newPassword: String): Boolean
}

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userRepository: UserRepository,
    private val networkMonitor: NetworkMonitor,
    private val notificationService: AppNotificationService
) : AuthRepository {

    companion object {
        private const val TAG = "AuthRepository"
    }

    override suspend fun signIn(email: String, password: String): User? {
        if (!networkMonitor.isOnline()) {
            notificationService.showError("Sem conexão com a internet. Não é possível realizar login.")
            return null
        }

        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return null
            
            // 1. Tenta buscar dados do usuário (Nome) no Firestore
            val userDoc = withTimeoutOrNull(5000) {
                firestore.collection("users").document(firebaseUser.uid).get().await()
            }
            val remoteName = userDoc?.getString("name") ?: ""

            // 2. Tenta buscar estatísticas locais primeiro (Prioridade Room)
            val localStats = userRepository.getStats(firebaseUser.uid)
            
            val finalStats = if (localStats != null) {
                // Se já temos stats locais, mantemos elas (Offline-first)
                localStats
            } else {
                // Se não há nada local, tentamos o Firestore como fallback
                val statsDoc = withTimeoutOrNull(5000) {
                    firestore.collection("users").document(firebaseUser.uid)
                        .collection("stats").document("global").get().await()
                }

                if (statsDoc != null && statsDoc.exists()) {
                    UserStats(
                        userId = firebaseUser.uid,
                        totalFlashcardsDone = statsDoc.getLong("totalFlashcardsDone")?.toInt() ?: 0,
                        totalFlashcardsMisses = statsDoc.getLong("totalFlashcardsMisses")?.toInt() ?: 0,
                        totalFlashcardsHits = statsDoc.getLong("totalFlashcardsHits")?.toInt() ?: 0,
                        lastReview = statsDoc.getLong("lastReview") ?: 0L,
                        totalStudyTime = statsDoc.getLong("totalStudyTime") ?: 0L,
                        totalFlashcardsReviewed = statsDoc.getLong("totalFlashcardsReviewed")?.toInt() ?: 0,
                        overallMastery = statsDoc.getDouble("overallMastery")?.toFloat() ?: 0f,
                        retentionRate = statsDoc.getDouble("retentionRate")?.toFloat() ?: 0f,
                        cognitiveEfficiencyIndex = statsDoc.getDouble("cognitiveEfficiencyIndex")?.toFloat() ?: 0f,
                        globalAverageLatencyMs = statsDoc.getLong("globalAverageLatencyMs") ?: 0L,
                        activeLeechesCount = statsDoc.getLong("activeLeechesCount")?.toInt() ?: 0
                    )
                } else {
                    UserStats(userId = firebaseUser.uid)
                }
            }

            val user = User(
                id = firebaseUser.uid,
                name = remoteName.ifEmpty { firebaseUser.displayName ?: "" },
                email = firebaseUser.email ?: "",
                stats = finalStats
            )

            // Salva/Atualiza no banco local apenas o que for necessário
            userRepository.updateUser(user)

            user
        } catch (e: FirebaseAuthException) {
            Log.e(TAG, "signIn: Firebase Auth Error code: ${e.errorCode}", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "signIn: Unknown Auth Error", e)
            null
        }
    }

    override suspend fun signUp(name: String, email: String, password: String): User? {
        if (!networkMonitor.isOnline()) {
            notificationService.showError("Sem conexão com a internet. Não é possível realizar o cadastro.")
            return null
        }

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
            } catch (e: Exception) {
                Log.e(TAG, "signUp: Firestore Sync Error", e)
            }

            // Salva no banco local (Room) para prioridade de uso offline
            userRepository.updateUser(newUser)

            newUser
        } catch (e: FirebaseAuthException) {
            Log.e(TAG, "signUp: Firebase Auth Error code: ${e.errorCode}", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "signUp: Unknown Auth Error", e)
            null
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun changePassword(newPassword: String): Boolean {
        return try {
            firebaseAuth.currentUser?.updatePassword(newPassword)?.await()
            true
        } catch (e: Exception) {
            Log.e(TAG, "changePassword: Error", e)
            false
        }
    }
}
