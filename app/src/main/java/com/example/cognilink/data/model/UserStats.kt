package com.example.cognilink.data.model

import com.example.cognilink.domain.model.CogniRank
import com.example.cognilink.domain.model.UserRankingResult

data class UserStats(
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
    val activeLeechesCount: Int,

    /**
     * RESULTADO DO RANKING: O posto atual do usuário baseado em seu desempenho.
     */
    val ranking: UserRankingResult
)

val fakeUserStats = UserStats(
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
    activeLeechesCount = 2,
    ranking = UserRankingResult(
        CogniRank.CONEXAO_SINAPTICA,
        finalScore = 88.0f,
        dynamicInsight = "Consistência ideal. Continue expandindo sua estabilidade de memória para alcançar a próxima patente.",
        efficiencyInsight = "Seu cérebro está absorvendo mais conteúdo em menos tempo.",
        retentionInsight = "Boa retenção. Continue revisando para fixar o conhecimento."
    )
)