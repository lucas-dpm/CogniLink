package com.lucasdpm.cognilink.data.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.lucasdpm.cognilink.data.repository.StudyContextRepository
import com.lucasdpm.cognilink.data.service.SystemNotificationService
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GeofenceBroadcastReceiver : BroadcastReceiver(), KoinComponent {

    private val studyContextRepository: StudyContextRepository by inject()
    private val systemNotificationService: SystemNotificationService by inject()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent == null) {
            Log.e(TAG, "GeofencingEvent is null")
            return
        }

        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)
            Log.e(TAG, "Geofencing error: $errorMessage")
            return
        }

        val geofenceTransition = geofencingEvent.geofenceTransition

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
            val triggeringGeofences = geofencingEvent.triggeringGeofences
            if (triggeringGeofences != null) {
                triggeringGeofences.forEach { geofence ->
                    handleGeofenceTrigger(geofence.requestId)
                }
            }
        }
    }

    private fun handleGeofenceTrigger(contextId: String) {
        scope.launch {
            try {
                val studyContext = studyContextRepository.getContextById(contextId)
                if (studyContext != null) {
                    val decks = studyContextRepository.getDecksForContext(contextId).first()
                    if (decks.isNotEmpty()) {
                        val deckNames = decks.joinToString(", ") { it.name }
                        systemNotificationService.showContextNotification(
                            title = "Local de Estudo Detectado!",
                            message = "Você está em ${studyContext.name}. Que tal revisar: $deckNames?"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error handling geofence trigger", e)
            }
        }
    }

    companion object {
        private const val TAG = "GeofenceReceiver"
    }
}
