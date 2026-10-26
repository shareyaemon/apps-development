package com.example

import android.app.Application
import android.util.Log
import com.example.data.FirebaseManager

class HilfulFujulApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            FirebaseManager.ensureFirebaseInitialized(this)
        } catch (t: Throwable) {
            Log.e("HilfulFujulApp", "Error during application onCreate Firebase init: ${t.message}", t)
        }
    }
}
