package com.lucasdpm.cognilink.ui.states

import com.lucasdpm.cognilink.data.model.StudyContext

data class SettingsUiState(
    val contexts: List<StudyContext> = emptyList(),
    val isLoading: Boolean = false,
    val isLoggedOut: Boolean = false
)
