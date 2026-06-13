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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserStats(stats: UserStatsEntity)

    @Update
    suspend fun updateUserStats(stats: UserStatsEntity)

    @Query("DELETE FROM users_stats WHERE userId = :userId")
    suspend fun deleteUserStatsByUserId(userId: String)

    @Query("DELETE FROM users_stats")
    suspend fun deleteAllUserStats()
}
