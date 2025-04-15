package com.android.androiddevelopertask

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AndroidDeveloperTaskApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Starting here")
    }

    companion object{
        const val TAG = "AndroidDeveloperTaskApplication"
    }
}
