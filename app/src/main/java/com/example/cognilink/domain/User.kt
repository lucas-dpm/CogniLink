package com.example.cognilink.domain

/**
 * Representa o usuário principal do sistema e seus dados agregados.
 */
data class User(
    val id: Long,
    val name: String,
    val email: String,
    val decks: List<Deck>,
    val stats: UserStats,
)

data class UserStats(
    val flashcardStatistics: List<FlashcardStatistics>,
    val totalFlashcardsMisses: Int,
    val totalFlashcardsHits: Int,
    val lastReview: Long,
    val totalStudyTime: Long,
    val totalFlashcardsDone: Int,
    val totalFlashcardsReviewed: Int,

    /**
     * MÉDIA DE DOMÍNIO GLOBAL: Média ponderada do mastery de todos os cards.
     */
    val overallMastery: Float,

    /**
     * TAXA DE RETENÇÃO GERAL: Porcentagem total de acertos do usuário.
     */
    val retentionRate: Float,

    /**
     * EFICIÊNCIA COGNITIVA: Estabilidade média dividida pelo tempo gasto.
     * Mostra se o usuário está retendo mais conhecimento em menos tempo de estudo.
     */
    val cognitiveEfficiencyIndex: Float,

    /**
     * LATÊNCIA MÉDIA GLOBAL: Tempo médio (ms) para responder a qualquer card.
     * Quedas nessa métrica indicam maior automação do conhecimento geral.
     */
    val globalAverageLatencyMs: Long,

    /**
     * DESEMPENHO POR CONTEXTO: Mapeia o ID ou nome do contexto (ex: "GEO_LIBRARY", "TIME_22PM")
     * para a taxa de retenção obtida nele. Revela onde o usuário estuda melhor.
     */
    val retentionByContext: Map<String, Float>,

    /**
     * CONVERSÃO DE GATILHOS: Porcentagem de notificações de contexto que o usuário
     * realmente aceitou e transformou em uma sessão de estudos.
     */
    val contextTriggerConversionRate: Float,

    /**
     * CONTADOR DE "SANGUESSUGAS" (LEECHES): Quantidade de cartões travados no limbo do esquecimento.
     * Alerta o usuário (ou o sistema) que esses conceitos precisam ser reestruturados.
     */
    val activeLeechesCount: Int
)

data class FlashcardStatistics(
    val idFlashcard: Long,
    val hits: Int,
    val misses: Int,
    val studyTime: Long,
    /**
     * LATÊNCIA DO CARD: Tempo médio que o usuário leva para evocar a resposta DESTE card.
     * Se o usuário acerta (hit), mas a latência é alta (ex: 7 segundos), o esforço cognitivo
     * ainda é alto. Se a latência for baixa (ex: 1.2 segundos), o conhecimento foi automatizado.
     */
    val averageLatencyMs: Long,

    /**
     * ESTABILIDADE DA MEMÓRIA (S): O intervalo atual calculado (em dias) em que a
     * probabilidade de recall deste card se mantém acima de 90%.
     */
    val memoryStabilityDays: Float,

    /**
     * FATOR DE FACILIDADE (Ease Factor - EF): Herdado do SM-2.
     * Mede o quão "fácil" ou "difícil" esse card é para o usuário com base no histórico.
     * Default começa em 2.5f. Diminui a cada erro e aumenta a cada acerto fácil.
     */
    val easeFactor: Float,

    /**
     * CONTEXTO ANCORA: O contexto onde este card específico performa melhor.
     * Útil para o CogniLink priorizar este card quando o usuário entrar neste cenário espacial/temporal.
     */
    val bestPerformingContext: String?,

    /**
     * CONTADOR DE FALHAS CONSECUTIVAS: Se atingir um limiar (ex: 4 ou 5), o card
     * é marcado como "Leech" (Sanguessuga).
     */
    val consecutiveMisses: Int,

    /**
     * TAXA DE RETENÇÃO INDIVIDUAL: Porcentagem de acertos em relação ao total de revisões.
     */
    val retentionRate: Float,

    /**
     * GRAU DE DOMÍNIO: Valor de 0.0 a 1.0 que representa o quão consolidado está o conhecimento.
     */
    val mastery: Float
)

val fakeFlashcardStatistics = listOf(
    FlashcardStatistics(
        idFlashcard = 1L,
        hits = 15,
        misses = 3,
        studyTime = 120000L,
        averageLatencyMs = 1500L,
        memoryStabilityDays = 4.5f,
        easeFactor = 2.6f,
        bestPerformingContext = "HOME_QUIET",
        consecutiveMisses = 0,
        retentionRate = 0.83f,
        mastery = 0.85f
    ),
    FlashcardStatistics(
        idFlashcard = 2L,
        hits = 5,
        misses = 8,
        studyTime = 300000L,
        averageLatencyMs = 7000L,
        memoryStabilityDays = 0.5f,
        easeFactor = 1.3f,
        bestPerformingContext = null,
        consecutiveMisses = 3,
        retentionRate = 0.38f,
        mastery = 0.20f
    )
)

val fakeUserStats = UserStats(
    flashcardStatistics = fakeFlashcardStatistics,
    totalFlashcardsMisses = 11,
    totalFlashcardsHits = 20,
    lastReview = 0L,
    totalStudyTime = 420000L,
    totalFlashcardsDone = 100,
    totalFlashcardsReviewed = 31,
    overallMastery = 0.65f,
    retentionRate = 0.64f,
    cognitiveEfficiencyIndex = 1.2f,
    globalAverageLatencyMs = 4250L,
    retentionByContext = mapOf(
        "GEO_LIBRARY" to 0.85f,
        "TIME_22PM" to 0.40f,
        "HOME_QUIET" to 0.92f
    ),
    contextTriggerConversionRate = 0.75f,
    activeLeechesCount = 2
)

val fakeUser = User(
    id = 1001L,
    name = "Alex Silva",
    email = "alex.silva@example.com",
    decks = listOf(deck1, deck2, deck3),
    stats = fakeUserStats
)





