package com.lucasdpm.cognilink.data.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.lucasdpm.cognilink.data.model.StudyContext
import com.lucasdpm.cognilink.domain.service.GeofenceManager
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

class AndroidGeofenceManager(private val context: Context) : GeofenceManager {

    private val geofencingClient = LocationServices.getGeofencingClient(context)

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    @SuppressLint("MissingPermission")
    override suspend fun addGeofence(studyContext: StudyContext) {
        val geofence = Geofence.Builder()
            .setRequestId(studyContext.id)
            .setCircularRegion(
                studyContext.latitude,
                studyContext.longitude,
                studyContext.radius
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL)
            .setLoiteringDelay(studyContext.dwellTimeMillis.toInt())
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL)
            .addGeofence(geofence)
            .build()

        try {
            geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent).await()
            Log.d(TAG, "Geofence added: ${studyContext.name}")
        } catch (e: Exception) {
            Log.e(TAG, "Error adding geofence: ${studyContext.name}", e)
        }
    }

    override suspend fun removeGeofence(contextId: String) {
        try {
            geofencingClient.removeGeofences(listOf(contextId)).await()
            Log.d(TAG, "Geofence removed: $contextId")
        } catch (e: Exception) {
            Log.e(TAG, "Error removing geofence: $contextId", e)
        }
    }

    override suspend fun removeAllGeofences() {
        try {
            geofencingClient.removeGeofences(geofencePendingIntent).await()
            Log.d(TAG, "All geofences removed")
        } catch (e: Exception) {
            Log.e(TAG, "Error removing all geofences", e)
        }
    }

    companion object {
        private const val TAG = "AndroidGeofenceManager"
    }
}
