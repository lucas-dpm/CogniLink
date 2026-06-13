package com.lucasdpm.cognilink.data.model

data class FlashcardStats(
    val flashcardId: String,
    val hits: Int = 0,
    val misses: Int = 0,
    val studyTime: Long = 0L,
    /**
     * PRÓXIMA REVISÃO: Timestamp (Unix Epoch) indicando quando este card deve ser
     * revisado novamente pelo usuário. Se nulo, o card nunca foi revisado.
     */
    val nextReview: Long? = null,
    /**
     * LATÊNCIA DO CARD: Tempo médio que o usuário leva para evocar a resposta DESTE card.
     * Se o usuário acerta (hit), mas a latência é alta (ex: 7 segundos), o esforço cognitivo
     * ainda é alto. Se a latência for baixa (ex: 1.2 segundos), o conhecimento foi automatizado.
     */
    val averageLatencyMs: Long = 0L,

    /**
     * ESTABILIDADE DA MEMÓRIA (S): O intervalo atual calculado (em dias) em que a
     * probabilidade de recall deste card se mantém acima de 90%.
     */
    val memoryStabilityDays: Float = 0f,

    /**
     * FATOR DE FACILIDADE (Ease Factor - EF): Herdado do SM-2.
     * Mede o quão "fácil" ou "difícil" esse card é para o usuário com base no histórico.
     * Default começa em 2.5f. Diminui a cada erro e aumenta a cada acerto fácil.
     */
    val easeFactor: Float = 0f,

    /**
     * CONTEXTO ANCORA: O contexto onde este card específico performa melhor.
     * Útil para o CogniLink priorizar este card quando o usuário entrar neste cenário espacial/temporal.
     */
    val bestPerformingContext: String? = null,

    /**
     * CONTADOR DE FALHAS CONSECUTIVAS: Se atingir um limiar (ex: 4 ou 5), o card
     * é marcado como "Leech" (Sanguessuga).
     */
    val consecutiveMisses: Int = 0,

    /**
     * TAXA DE RETENÇÃO INDIVIDUAL: Porcentagem de acertos em relação ao total de revisões.
     */
    val retentionRate: Float = 0f,

    /**
     * GRAU DE DOMÍNIO: Valor de 0.0 a 1.0 que representa o quão consolidado está o conhecimento.
     */
    val mastery: Float = 0f
)
