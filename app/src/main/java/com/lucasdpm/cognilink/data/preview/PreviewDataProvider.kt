package com.lucasdpm.cognilink.data.preview

import com.lucasdpm.cognilink.data.model.*
import com.lucasdpm.cognilink.domain.model.*

/**
 * Provedor de dados fakes para Previews do Jetpack Compose e testes de UI.
 * Os IDs são consistentes entre os objetos para simular relacionamentos de banco de dados.
 */
object PreviewDataProvider {
    
    // IDs consistentes para comunicação entre modelos
    const val USER_ID = "user-123"
    const val DECK_ID_1 = "deck-kotlin-001"
    const val DECK_ID_2 = "deck-historia-002"
    const val FLASHCARD_ID_1 = "fc-coroutine-101"
    const val FLASHCARD_ID_2 = "fc-flow-102"
    const val FLASHCARD_ID_3 = "fc-ww1-201"

    /**
     * Objeto de Usuário fake
     */
    val user = User(
        id = USER_ID,
        name = "Lucas Martins",
        email = "lucas@example.com"
    )

    /**
     * Estatísticas globais do usuário
     */
    val userStats = UserStats(
        userId = USER_ID,
        totalFlashcardsHits = 150,
        totalFlashcardsMisses = 30,
        totalFlashcardsDone = 180,
        totalFlashcardsReviewed = 45,
        totalStudyTime = 3600000L,
        overallMastery = 0.82f,
        retentionRate = 0.85f,
        cognitiveEfficiencyIndex = 0.75f,
        globalAverageLatencyMs = 2500L,
        lastReview = System.currentTimeMillis(),
        activeLeechesCount = 2,
        retentionByContext = mapOf("Home" to 0.88f, "Work" to 0.72f)
    )

    /**
     * Resultado de ranking do usuário
     */
    val userRanking = UserRankingResult(
        currentRank = UserRank.SILICON_MEMORY,
        finalScore = 65.5f,
        dynamicInsight = "Sua memória está se tornando altamente estável.",
        efficiencyInsight = "Você responde rápido, mas cuidado com a precisão.",
        retentionInsight = "Retenção excelente em temas de tecnologia."
    )

    /**
     * Lista de Decks fakes
     */
    val deckList = listOf(
        Deck(
            id = DECK_ID_1,
            userId = USER_ID,
            name = "Kotlin Avançado",
            categories = listOf("Programação", "Android", "Kotlin"),
            description = "Conceitos complexos de Kotlin Coroutines, Flow e Memory Management.",
            difficulty = DifficultyLevel.HARD,
            mastery = 0.45f,
            totalCards = 25,
            cardsToReview = 12
        ),
        Deck(
            id = DECK_ID_2,
            userId = USER_ID,
            name = "História Geral",
            categories = listOf("Humanas", "História", "Século XX"),
            description = "Principais eventos que moldaram o mundo no século XX.",
            difficulty = DifficultyLevel.MEDIUM,
            mastery = 0.78f,
            totalCards = 50,
            cardsToReview = 5
        )
    )

    /**
     * Lista de Flashcards fakes
     */
    val flashcardList = listOf(
        Flashcard(
            id = FLASHCARD_ID_1,
            deckId = DECK_ID_1,
            question = "O que é um 'suspend function' em Kotlin?",
            cardType = FlashcardType.BASIC,
            difficulty = DifficultyLevel.MEDIUM,
            answerOptions = listOf(
                Answer("Uma função que pode ser pausada e retomada sem bloquear a thread.", true)
            ),
            hints = listOf("Relacionado a corrotinas", "Eficiência de recursos")
        ),
        Flashcard(
            id = FLASHCARD_ID_2,
            deckId = DECK_ID_1,
            question = "Qual operador é usado para emitir valores em um Flow?",
            cardType = FlashcardType.MULTIPLE_CHOICE,
            difficulty = DifficultyLevel.EASY,
            answerOptions = listOf(
                Answer("emit()", true),
                Answer("send()", false),
                Answer("post()", false),
                Answer("push()", false)
            ),
            hints = listOf("Oposto de collect")
        ),
        Flashcard(
            id = FLASHCARD_ID_3,
            deckId = DECK_ID_2,
            question = "A Primeira Guerra Mundial terminou no ano de [[1918]].",
            cardType = FlashcardType.OMISSION,
            difficulty = DifficultyLevel.EASY,
            answerOptions = listOf(
                Answer("1918", true)
            ),
            hints = listOf("Início do século XX")
        )
    )

    /**
     * Estatísticas individuais de Flashcards
     */
    val flashcardStatsList = listOf(
        FlashcardStats(
            flashcardId = FLASHCARD_ID_1,
            hits = 5,
            misses = 1,
            studyTime = 60000L,
            nextReview = System.currentTimeMillis() + 86400000L, // +1 dia
            averageLatencyMs = 3500L,
            memoryStabilityDays = 3.5f,
            easeFactor = 2.4f,
            mastery = 0.6f,
            retentionRate = 0.83f,
            consecutiveMisses = 0
        ),
        FlashcardStats(
            flashcardId = FLASHCARD_ID_2,
            hits = 10,
            misses = 0,
            studyTime = 30000L,
            nextReview = System.currentTimeMillis() + 604800000L, // +1 semana
            averageLatencyMs = 1200L,
            memoryStabilityDays = 14.0f,
            easeFactor = 2.8f,
            mastery = 0.95f,
            retentionRate = 1.0f,
            consecutiveMisses = 0
        )
    )

    /**
     * Atalhos para acesso rápido a um único item
     */
    val deck = deckList.first()
    val flashcard = flashcardList.first()
    val flashcardStats = flashcardStatsList.first()
}
