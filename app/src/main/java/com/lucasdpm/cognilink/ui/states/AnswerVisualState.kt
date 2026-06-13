package com.lucasdpm.cognilink.ui.states

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.Green
import com.lucasdpm.cognilink.ui.theme.MutedBlue
import com.lucasdpm.cognilink.ui.theme.OffWhite
import com.lucasdpm.cognilink.ui.theme.Red

sealed interface AnswerVisualState {
    object Default : AnswerVisualState
    object Selected : AnswerVisualState
    object Correct : AnswerVisualState
    object Incorrect : AnswerVisualState
}

val AnswerVisualState.border: BorderStroke?
    @Composable get() = when (this) {
        AnswerVisualState.Selected -> BorderStroke(2.dp, DarkNavyBlue)
        AnswerVisualState.Correct -> BorderStroke(2.dp, Green)
        AnswerVisualState.Incorrect -> BorderStroke(2.dp, Red)
        AnswerVisualState.Default -> null
    }

val AnswerVisualState.labelBackgroundColor: Color
    @Composable get() = when (this) {
        AnswerVisualState.Selected -> MutedBlue
        AnswerVisualState.Correct -> Green.copy(alpha = 0.2f)
        AnswerVisualState.Incorrect -> Red.copy(alpha = 0.2f)
        AnswerVisualState.Default -> OffWhite
    }

val AnswerVisualState.labelTextColor: Color
    get() = when (this) {
        AnswerVisualState.Correct -> Green
        AnswerVisualState.Incorrect -> Red
        else -> DarkNavyBlue
    }
