package com.lucasdpm.cognilink.domain.repository

interface NetworkMonitor {
    fun isOnline(): Boolean
    fun isWifiConnected(): Boolean
}
