package com.lucasdpm.cognilink.domain.model

data class UserRankingResult(
    val currentRank: UserRank,
    val finalScore: Float, // De 0.0 a 100.0
    val dynamicInsight: String,
    val efficiencyInsight: String,
    val retentionInsight: String
)