package com.lucasdpm.cognilink.ui.states

import androidx.annotation.DrawableRes
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.ui.graphics.Color
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.theme.Green
import com.lucasdpm.cognilink.ui.theme.Red
import com.lucasdpm.cognilink.ui.theme.Yellow

enum class SnackbarType {
    ERROR, SUCCESS, WARNING, INFO
}

data class SnackbarMessage(
    val message: String,
    val type: SnackbarType = SnackbarType.ERROR,
    val actionLabel: String? = null,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val withDismissAction: Boolean = true
)

class CustomSnackbarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    override val duration: SnackbarDuration = SnackbarDuration.Short,
    override val withDismissAction: Boolean = true,
    val type: SnackbarType = SnackbarType.ERROR
) : SnackbarVisuals {
    
    @get:DrawableRes
    val icon: Int
        get() = when (type) {
            SnackbarType.ERROR -> R.drawable.ic_cancel
            SnackbarType.SUCCESS -> R.drawable.ic_check_circle
            SnackbarType.WARNING -> R.drawable.ic_warning
            SnackbarType.INFO -> R.drawable.ic_info // Fallback se não tiver info
        }

    val iconColor: Color
        get() = when (type) {
            SnackbarType.ERROR -> Red
            SnackbarType.SUCCESS -> Green
            SnackbarType.WARNING -> Yellow
            SnackbarType.INFO -> DarkNavyBlue
        }
}
