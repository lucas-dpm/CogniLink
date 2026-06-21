package com.lucasdpm.cognilink.domain.service

import com.lucasdpm.cognilink.data.model.StudyContext

interface GeofenceManager {
    suspend fun addGeofence(context: StudyContext)
    suspend fun removeGeofence(contextId: String)
    suspend fun removeAllGeofences()
}
