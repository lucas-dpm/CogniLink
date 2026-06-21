package com.lucasdpm.cognilink.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.lucasdpm.cognilink.data.model.StudyContext
import com.lucasdpm.cognilink.data.repository.StudyContextRepository
import com.lucasdpm.cognilink.domain.service.AppNotificationService
import com.lucasdpm.cognilink.domain.service.GeofenceManager
import com.lucasdpm.cognilink.ui.states.ContextFormUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ContextFormViewModel(
    private val studyContextRepository: StudyContextRepository,
    private val geofenceManager: GeofenceManager,
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val notificationService: AppNotificationService
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContextFormUiState())
    val uiState: StateFlow<ContextFormUiState> = _uiState.asStateFlow()

    fun initialize(userId: String, contextId: String?, deckId: String? = null) {
        _uiState.update { it.copy(userId = userId, id = contextId, deckId = deckId) }
        
        if (contextId != null) {
            loadContext(contextId)
        } else {
            loadCurrentLocation()
        }
    }

    private fun loadContext(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val context = studyContextRepository.getContextById(id)
            if (context != null) {
                _uiState.update {
                    it.copy(
                        name = context.name,
                        latitude = context.latitude,
                        longitude = context.longitude,
                        radius = context.radius,
                        isLoading = false,
                        initialLocationLoaded = true
                    )
                }
            }
        }
    }

    private fun loadCurrentLocation() {
        viewModelScope.launch {
            try {
                var location = fusedLocationProviderClient.lastLocation.await()

                if (location == null) {
                    val request = CurrentLocationRequest.Builder()
                        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                        .build()
                    location = fusedLocationProviderClient.getCurrentLocation(request, null).await()
                }

                if (location != null) {
                    _uiState.update {
                        it.copy(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            initialLocationLoaded = true
                        )
                    }
                }
            } catch (e: SecurityException) {
                Log.e("ContextFormVM", "Permission denied", e)
            }
        }
    }

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName, nameError = null) }
    }

    fun onLocationChange(lat: Double, lng: Double) {
        _uiState.update { it.copy(latitude = lat, longitude = lng) }
    }

    fun saveContext() {
        if (_uiState.value.name.isBlank()) {
            _uiState.update { it.copy(nameError = "O nome é obrigatório") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                val state = _uiState.value
                val contextId = state.id ?: UUID.randomUUID().toString()
                val context = StudyContext(
                    id = contextId,
                    userId = state.userId,
                    name = state.name,
                    latitude = state.latitude,
                    longitude = state.longitude,
                    radius = state.radius,
                    dwellTimeMillis = 600000
                )
                studyContextRepository.saveContext(context)
                geofenceManager.addGeofence(context)

                // Se houver um deckId, vincula o novo local ao baralho
                state.deckId?.let { dId ->
                    studyContextRepository.linkDeckToContext(dId, contextId)
                }
                
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
                notificationService.showSuccess("Local de estudo salvo com sucesso!")
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false) }
                notificationService.showError("Erro ao salvar local de estudo.")
            }
        }
    }
}
