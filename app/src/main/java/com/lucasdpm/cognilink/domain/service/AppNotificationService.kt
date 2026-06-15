package com.lucasdpm.cognilink.domain.service

import com.lucasdpm.cognilink.ui.states.SnackbarMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AppNotificationService {
    private val _snackbarMessages = MutableSharedFlow<SnackbarMessage>()
    val snackbarMessages = _snackbarMessages.asSharedFlow()

    suspend fun showSnackbar(message: SnackbarMessage) {
        _snackbarMessages.emit(message)
    }

    suspend fun showSuccess(message: String) {
        showSnackbar(SnackbarMessage(message, type = com.lucasdpm.cognilink.ui.states.SnackbarType.SUCCESS))
    }

    suspend fun showError(message: String) {
        showSnackbar(SnackbarMessage(message, type = com.lucasdpm.cognilink.ui.states.SnackbarType.ERROR))
    }

    suspend fun showWarning(message: String) {
        showSnackbar(SnackbarMessage(message, type = com.lucasdpm.cognilink.ui.states.SnackbarType.WARNING))
    }
}
