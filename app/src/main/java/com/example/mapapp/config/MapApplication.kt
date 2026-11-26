package com.example.mapapp.config

import android.app.Application

class MapApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MapConfig.initialize(this)
    }
}
