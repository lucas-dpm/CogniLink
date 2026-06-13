package com.lucasdpm.cognilink.data.datebase.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards_stats",
    foreignKeys = [
        ForeignKey(
            entity = FlashcardEntity::class,
            parentColumns = ["id"],
            childColumns = ["flashcardId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class FlashcardStatsEntity(
    @PrimaryKey
    val flashcardId: String,
    val hits: Int = 0,
    val misses: Int = 0,
    val studyTime: Long = 0L,
    val nextReview: Long? = null,
    val averageLatencyMs: Long = 0L,
    val memoryStabilityDays: Float = 0f,
    val easeFactor: Float = 0f,
    val bestPerformingContext: String? = null,
    val consecutiveMisses: Int = 0,
    val retentionRate: Float = 0f,
    val mastery: Float = 0f
)
