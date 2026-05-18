package com.example.cognilink.domain

enum class DifficultyLevel(val weight: Int) {
    EASY(1), MEDIUM(2), HARD(3);

    fun toDisplayName() = when(this) {
        EASY -> "FÁCIL"
        MEDIUM -> "MÉDIO"
        HARD -> "DIFÍCIL"
    }

    companion object {
        fun fromWeight(weight: Int): DifficultyLevel {
            return entries.find { it.weight == weight } ?: MEDIUM
        }
        
        fun fromAverage(average: Float): DifficultyLevel {
            return when {
                average <= 1.5f -> EASY
                average <= 2.5f -> MEDIUM
                else -> HARD
            }
        }
    }
}