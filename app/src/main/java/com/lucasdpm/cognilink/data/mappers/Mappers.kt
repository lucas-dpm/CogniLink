package com.lucasdpm.cognilink.data.mappers

import com.lucasdpm.cognilink.data.datebase.entities.*
import com.lucasdpm.cognilink.data.model.*

import com.lucasdpm.cognilink.domain.model.DifficultyLevel

// User Mappers
fun UserEntity.toDomain(stats: UserStats): User = User(
    id = id,
    name = name,
    email = email,
    stats = stats
)

fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    name = name,
    email = email
)

// UserStats Mappers
fun UserStatsEntity.toDomain(): UserStats = UserStats(
    userId = userId,
    totalFlashcardsMisses = totalFlashcardsMisses,
    totalFlashcardsHits = totalFlashcardsHits,
    lastReview = lastReview,
    totalStudyTime = totalStudyTime,
    totalFlashcardsDone = totalFlashcardsDone,
    totalFlashcardsReviewed = totalFlashcardsReviewed,
    overallMastery = overallMastery,
    retentionRate = retentionRate,
    cognitiveEfficiencyIndex = cognitiveEfficiencyIndex,
    globalAverageLatencyMs = globalAverageLatencyMs,
    retentionByContext = retentionByContext,
    contextTriggerConversionRate = contextTriggerConversionRate,
    activeLeechesCount = activeLeechesCount
)

fun UserStats.toEntity(): UserStatsEntity = UserStatsEntity(
    userId = userId,
    totalFlashcardsMisses = totalFlashcardsMisses,
    totalFlashcardsHits = totalFlashcardsHits,
    lastReview = lastReview,
    totalStudyTime = totalStudyTime,
    totalFlashcardsDone = totalFlashcardsDone,
    totalFlashcardsReviewed = totalFlashcardsReviewed,
    overallMastery = overallMastery,
    retentionRate = retentionRate,
    cognitiveEfficiencyIndex = cognitiveEfficiencyIndex,
    globalAverageLatencyMs = globalAverageLatencyMs,
    retentionByContext = retentionByContext,
    contextTriggerConversionRate = contextTriggerConversionRate,
    activeLeechesCount = activeLeechesCount
)

// Deck Mappers
fun DeckEntity.toDomain(
    totalCards: Int = 0,
    cardsToReview: Int = 0,
    difficulty: DifficultyLevel = DifficultyLevel.EASY,
    mastery: Float = 0f
): Deck = Deck(
    id = id,
    userId = userId,
    name = name,
    categories = categories,
    description = description,
    difficulty = difficulty,
    mastery = mastery,
    totalCards = totalCards,
    cardsToReview = cardsToReview
)

fun DeckWithStatsEntity.toDomain(): Deck = deck.toDomain(
    totalCards = totalCards,
    cardsToReview = cardsToReview,
    difficulty = DifficultyLevel.fromAverage(averageDifficulty),
    mastery = averageMastery
)

fun Deck.toEntity(): DeckEntity = DeckEntity(
    id = id,
    userId = userId,
    name = name,
    categories = categories,
    description = description
)

// Flashcard Mappers
fun FlashcardEntity.toDomain(): Flashcard = Flashcard(
    id = id,
    deckId = deckId,
    question = question,
    cardType = cardType,
    difficulty = difficulty,
    answerOptions = answerOptions,
    hints = hints
)

fun Flashcard.toEntity(): FlashcardEntity = FlashcardEntity(
    id = id,
    deckId = deckId,
    question = question,
    cardType = cardType,
    difficulty = difficulty,
    answerOptions = answerOptions,
    hints = hints
)

fun FlashcardWithStatsEntity.toDomain(): FlashcardWithStats = FlashcardWithStats(
    flashcard = flashcard.toDomain(),
    stats = stats?.toDomain()
)

// FlashcardStats Mappers
fun FlashcardStatsEntity.toDomain(): FlashcardStats = FlashcardStats(
    flashcardId = flashcardId,
    hits = hits,
    misses = misses,
    studyTime = studyTime,
    nextReview = nextReview,
    averageLatencyMs = averageLatencyMs,
    memoryStabilityDays = memoryStabilityDays,
    easeFactor = easeFactor,
    bestPerformingContext = bestPerformingContext,
    consecutiveMisses = consecutiveMisses,
    retentionRate = retentionRate,
    mastery = mastery
)

fun FlashcardStats.toEntity(): FlashcardStatsEntity = FlashcardStatsEntity(
    flashcardId = flashcardId,
    hits = hits,
    misses = misses,
    studyTime = studyTime,
    nextReview = nextReview,
    averageLatencyMs = averageLatencyMs,
    memoryStabilityDays = memoryStabilityDays,
    easeFactor = easeFactor,
    bestPerformingContext = bestPerformingContext,
    consecutiveMisses = consecutiveMisses,
    retentionRate = retentionRate,
    mastery = mastery
)
