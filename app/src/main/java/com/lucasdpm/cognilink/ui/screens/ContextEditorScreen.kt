package com.lucasdpm.cognilink.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.lucasdpm.cognilink.R
import com.lucasdpm.cognilink.ui.components.input.CustomTextField
import com.lucasdpm.cognilink.ui.components.utils.FullScreenLoading
import com.lucasdpm.cognilink.ui.components.utils.NavigationHeader
import com.lucasdpm.cognilink.ui.components.utils.buttons.SimpleGradientButton
import com.lucasdpm.cognilink.ui.theme.DarkNavyBlue
import com.lucasdpm.cognilink.ui.viewmodels.ContextFormViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ContextEditorScreen(
    userId: String,
    contextId: String? = null,
    deckId: String? = null,
    onNavigateBack: () -> Unit,
    viewModel: ContextFormViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(userId, contextId, deckId) {
        viewModel.initialize(userId, contextId, deckId)
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onNavigateBack()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ContextEditorContent(
            name = uiState.name,
            nameError = uiState.nameError,
            onNameChange = viewModel::onNameChange,
            latitude = uiState.latitude,
            longitude = uiState.longitude,
            onLocationChange = viewModel::onLocationChange,
            isEditMode = contextId != null,
            initialLocationLoaded = uiState.initialLocationLoaded,
            onSave = viewModel::saveContext,
            onBackClick = onNavigateBack
        )

        if (uiState.isSaving || uiState.isLoading) {
            FullScreenLoading()
        }
    }
}

@Composable
fun ContextEditorContent(
    name: String,
    nameError: String?,
    onNameChange: (String) -> Unit,
    latitude: Double,
    longitude: Double,
    onLocationChange: (Double, Double) -> Unit,
    isEditMode: Boolean,
    initialLocationLoaded: Boolean,
    onSave: () -> Unit,
    onBackClick: () -> Unit
) {
    val cameraPositionState = rememberCameraPositionState()

    // Atualiza a câmera apenas quando a localização inicial é carregada
    LaunchedEffect(initialLocationLoaded) {
        if (initialLocationLoaded) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(latitude, longitude), 15f)
        }
    }

    // Sincroniza a posição do marcador com o centro do mapa quando o usuário navega
    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            onLocationChange(
                cameraPositionState.position.target.latitude,
                cameraPositionState.position.target.longitude
            )
        }
    }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            NavigationHeader(
                title = if (isEditMode) "Editar Localização" else "Nova Localização",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.padding(24.dp),
                color = Color.Transparent
            ) {
                SimpleGradientButton(
                    text = "SALVAR LOCALIZAÇÃO",
                    height = 48.dp,
                    icon = R.drawable.ic_check,
                    onClickButton = onSave
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                CustomTextField(
                    inputValue = name,
                    onInputValueChange = onNameChange,
                    placeholder = "Ex: Biblioteca Central, Minha Casa...",
                    label = { Text("Nome da Localização", fontWeight = FontWeight.Bold, color = DarkNavyBlue) },
                    errorMessage = nameError
                )
            }

            Box(modifier = Modifier.weight(1f).padding(horizontal = 24.dp).padding(bottom = 24.dp)) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    if (initialLocationLoaded) {
                        val markerState = remember(latitude, longitude) {
                            MarkerState(position = LatLng(latitude, longitude))
                        }
                        
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraPositionState,
                            onMapClick = { latLng ->
                                onLocationChange(latLng.latitude, latLng.longitude)
                                cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, cameraPositionState.position.zoom)
                            }
                        ) {
                            Marker(
                                state = markerState,
                                title = name.ifBlank { "Local de Estudo" }
                            )
                        }
                    }
                }
            }
        }
    }
}
