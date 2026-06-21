package com.lucasdpm.cognilink.data.model

data class StudyContext(
    val id: String,
    val userId: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val radius: Float,
    val dwellTimeMillis: Long
)
