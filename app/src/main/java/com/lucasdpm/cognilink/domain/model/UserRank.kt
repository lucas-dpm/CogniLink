package com.lucasdpm.cognilink.domain.model

enum class UserRank(val displayName: String, val minScore: Float) {
    BEGINNER_NEURON("Neurônio Iniciante", 0.0f),
    SYNAPTIC_CONNECTION("Conexão Sináptica", 25.0f),
    SILICON_MEMORY("Memória de Silício", 50.0f),
    RECALL_MASTER("Mestre do Recall", 75.0f),
    COGNITIVE_ARCHITECT("Arquiteto Cognitivo", 90.0f);

    companion object {
        fun fromScore(score: Float): UserRank {
            return entries.lastOrNull { score >= it.minScore } ?: BEGINNER_NEURON
        }
    }
}

