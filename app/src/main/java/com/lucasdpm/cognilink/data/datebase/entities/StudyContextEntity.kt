package com.lucasdpm.cognilink.data.datebase.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "study_contexts")
data class StudyContextEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val radius: Float = 50f,
    val dwellTimeMillis: Long = 600000 // 10 minutos padrão
)
