package com.lucasdpm.cognilink.domain.model

data class SM2Result(
    val intervalDays: Float,
    val easeFactor: Float,
    val nextReview: Long,
    val repetitions: Int
)
