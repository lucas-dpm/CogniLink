package com.lucasdpm.cognilink.data.model

import com.lucasdpm.cognilink.domain.model.UserRankingResult

data class UserStats(
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