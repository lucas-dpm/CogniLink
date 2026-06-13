package com.lucasdpm.cognilink.ui.states

import com.lucasdpm.cognilink.data.model.UserStats
import com.lucasdpm.cognilink.domain.model.UserRankingResult

sealed interface ProfileUiState {
    object Loading : ProfileUiState
    data class Success(
        val userId: String,
        val userName: String,
        val stats: UserStats,
        val ranking: UserRankingResult
    ) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}
