package com.example.cognilink.domain

data class Deck(
    val id: Long,
    val name:String,
    val categories: List<String>,
    val description: String,
    val difficulty: DifficultyLevel,
    val mastery: Float,
    val totalCards: Int,
    val cardsToReview: Int,
    val flashcards: List<Flashcard>
)

// Fake Object
val deck1 = Deck(
    id = 1L,
    name = "Geografia Mundial",
    categories = listOf("Geografia", "História"),
    description = "Um deck com perguntas sobre geografia mundial",
    difficulty = DifficultyLevel.MEDIUM,
    mastery = 0.75f,
    totalCards = 1,
    cardsToReview = 1,
    flashcards = listOf(flashcard1)
)

val deck2 = Deck(
    id = 2L,
    name = "História Antiga",
    categories = listOf("História", "Arte"),
    description = "Um deck com perguntas sobre história antiga",
    difficulty = DifficultyLevel.HARD,
    mastery = 0.9f,
    totalCards = 1,
    cardsToReview = 0,
    flashcards = listOf(flashcard2)
)

val deck3 = Deck(
    id = 3L,
    name = "Conhecimentos Gerais",
    categories = listOf("Ciências", "Matemática"),
    description = "Um deck com perguntas gerais sobre ciência e matemática",
    difficulty = DifficultyLevel.EASY,
    mastery = 0.5f,
    totalCards = 1,
    cardsToReview = 1,
    flashcards = listOf(flashcard1,flashcard2,flashcard3,flashcard4)
)