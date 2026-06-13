package com.lucasdpm.cognilink.data.model

import com.lucasdpm.cognilink.domain.model.DifficultyLevel
import java.util.UUID

data class Deck(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val name:String,
    val categories: List<String>,
    val description: String,
    val difficulty: DifficultyLevel,
    val mastery: Float,
    val totalCards: Int,
    val cardsToReview: Int
)
