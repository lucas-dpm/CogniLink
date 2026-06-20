package com.lucasdpm.cognilink.data.datebase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lucasdpm.cognilink.data.datebase.entities.UserStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserStatsDao {
    @Query("SELECT * FROM users_stats WHERE userId = :userId")
    fun getUserStatsByUserId(userId: String): Flow<UserStatsEntity?>

    @Query("SELECT * FROM users_stats WHERE userId = :userId")
    suspend fun getUserStatsById(userId: String): UserStatsEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserStats(stats: UserStatsEntity): Long

    @Update
    suspend fun updateUserStats(stats: UserStatsEntity)

    @androidx.room.Transaction
    suspend fun saveUserStats(stats: UserStatsEntity) {
        val id = insertUserStats(stats)
        if (id == -1L) {
            updateUserStats(stats)
        }
    }

    @Query("DELETE FROM users_stats WHERE userId = :userId")
    suspend fun deleteUserStatsByUserId(userId: String)

    @Query("DELETE FROM users_stats")
    suspend fun deleteAllUserStats()
}
