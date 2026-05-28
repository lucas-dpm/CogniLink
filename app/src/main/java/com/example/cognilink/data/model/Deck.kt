package com.example.cognilink.data.model

import com.example.cognilink.domain.model.DifficultyLevel

data class Deck(
    val id: Long,
    val userId: Long, // Referência ao dono do deck
    val name:String,
    val categories: List<String>,
    val description: String,
    val difficulty: DifficultyLevel,
    val mastery: Float,
    val totalCards: Int,
    val cardsToReview: Int
)

// Fake Object
val deck1 = Deck(
    id = 1L,
    userId = 1001L,
    name = "Geografia Mundial",
    categories = listOf("Geografia", "História"),
    description = "Um deck com perguntas sobre geografia mundial",
    difficulty = DifficultyLevel.MEDIUM,
    mastery = 0.75f,
    totalCards = 1,
    cardsToReview = 1
)

val deck2 = Deck(
    id = 2L,
    userId = 1001L,
    name = "História Antiga",
    categories = listOf("História", "Arte"),
    description = "Um deck com perguntas sobre história antiga",
    difficulty = DifficultyLevel.HARD,
    mastery = 0.9f,
    totalCards = 1,
    cardsToReview = 0
)

val deck3 = Deck(
    id = 3L,
    userId = 1001L,
    name = "Conhecimentos Gerais",
    categories = listOf("Ciências", "Matemática"),
    description = "Um deck com perguntas gerais sobre ciência e matemática",
    difficulty = DifficultyLevel.EASY,
    mastery = 0.5f,
    totalCards = 1,
    cardsToReview = 1
)
