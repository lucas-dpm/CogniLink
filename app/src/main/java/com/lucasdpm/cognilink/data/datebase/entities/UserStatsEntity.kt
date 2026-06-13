package com.lucasdpm.cognilink.data.datebase.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lucasdpm.cognilink.domain.model.UserRankingResult

@Entity(
    tableName = "users_stats",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserStatsEntity(
    @PrimaryKey
    val userId: String,
    val totalFlashcardsMisses: Int = 0,
    val totalFlashcardsHits: Int = 0,
    val lastReview: Long = 0L,
    val totalStudyTime: Long = 0L,
    val totalFlashcardsDone: Int = 0,
    val totalFlashcardsReviewed: Int = 0,
    val overallMastery: Float = 0f,
    val retentionRate: Float = 0f,
    val cognitiveEfficiencyIndex: Float = 0f,
    val globalAverageLatencyMs: Long = 0L,
    val retentionByContext: Map<String, Float> = emptyMap(),
    val contextTriggerConversionRate: Float = 0f,
    val activeLeechesCount: Int = 0,
    val ranking: UserRankingResult? = null
)
