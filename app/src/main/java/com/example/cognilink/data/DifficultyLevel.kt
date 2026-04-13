package com.example.cognilink.data

enum class DifficultyLevel {
    EAZY, MEDIUM, HARD;

    fun toDisplayName() = when(this) {
        EAZY -> "Fácil"
        MEDIUM -> "Médio"
        HARD -> "Difícil"
    }
}