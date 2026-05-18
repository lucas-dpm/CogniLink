package com.example.cognilink.domain

import java.util.Date

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val password: String,
    val decks: List<Deck>,
    val stats: UserStats,
    val gamification: UserGamification
)

data class UserGamification(
    val currentStreak: Int,
    val longestStreak: Int,
    val dailyGoalCards: Int,
    val lastActiveDate: Date?
)

data class UserStats(
    val flashcardStatistics: List<FlashcardStatistics>,
    val overallMastery: Float,
    val totalFlashcardsToReview: Int,
    val totalFlashcardsMisses: Int,
    val totalFlashcardsHits: Int,
    val totalDaysSinceLastReview: Int,
    val totalStudyTime: Long,
    val totalFlashcards: Int,
    val totalFlashcardsReviewed: Int,
    val retentionRate: Float
)

data class FlashcardStatistics(
    val idFlashcard: Long,
    val lastReviewDate: Date,
    val nextReviewDate: Date,
    val hits: Int,
    val misses: Int,
    val studyTime: Long,
    val retentionRate: Float,
    val mastery: Float,
)


