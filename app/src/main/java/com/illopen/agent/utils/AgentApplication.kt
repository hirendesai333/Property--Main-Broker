package com.illopen.agent.utils

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AgentApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AppPreferences.initialize(this)
    }

}