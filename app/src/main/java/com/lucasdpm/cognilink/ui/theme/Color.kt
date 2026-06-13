package com.lucasdpm.cognilink.ui.theme

import androidx.compose.ui.graphics.Color
import com.lucasdpm.cognilink.domain.model.DifficultyLevel

val DarkNavyBlue = Color(0xFF1A237E)

var DarkBlue = Color(0xFF141F38)

val MutedBlue = Color(0xFFD2D4FF)

val LavenderBlue = Color(0xFF8690EE)

val LightNavyBlue = Color(0xFFE0E0FF)

val VividCyan = Color(0xFF22D3EE)

val DarkGray = Color(0xFF454652)

val VeryDarkGray = Color(0xFF1A1C1C)

val Gray = Color(0xFF767683)

val LightGray = Color(0xFFC6C5D4)

val VeryLightGray = Color(0xFFF1F1F1)

val Black = Color(0xFF000000)

val White = Color(0xFFFFFFFF)

val OffWhite = Color(0xFFF9F9F9)

val DarkGreen = Color(0xFF15803D)

val Green = Color(0xFF22C55E)

val VeryLightGreen = Color(0xFFDCFCE7)

val DarkRed = Color(0xFF380B00)

val Red = Color(0xFFBA1A1A)

val VeryLightRed = Color(0xFFFFDAD6)

val DarkYellow = Color(0xFFFFA500)

val Yellow = Color(0xFFFFBF00)

val VeryLightYellow = Color(0xFFFFFBDA)

val DifficultyLevel.primaryColor: Color
    get() = when (this) {
        DifficultyLevel.EASY -> DarkGreen
        DifficultyLevel.MEDIUM -> DarkYellow
        DifficultyLevel.HARD -> DarkRed
    }

val DifficultyLevel.secondaryColor: Color
    get() = when (this) {
        DifficultyLevel.EASY -> Green
        DifficultyLevel.MEDIUM -> Yellow
        DifficultyLevel.HARD -> Red
    }

val DifficultyLevel.tertiaryColor: Color
    get() = when (this) {
        DifficultyLevel.EASY -> VeryLightGreen
        DifficultyLevel.MEDIUM -> VeryLightYellow
        DifficultyLevel.HARD -> VeryLightRed
    }
