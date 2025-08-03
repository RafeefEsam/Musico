package com.example.musico

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MusicoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize any libraries or components here
        // For example, you might initialize a dependency injection framework
        // or set up logging.
    }
}