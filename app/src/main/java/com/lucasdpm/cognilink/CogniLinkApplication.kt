package com.lucasdpm.cognilink

import android.app.Application
import com.lucasdpm.cognilink.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CogniLinkApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@CogniLinkApplication)
            // Load modules
            modules(appModule)
        }
    }
}
