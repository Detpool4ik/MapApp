package com.example.mapapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mapapp.config.MapConfig
import com.example.mapapp.controller.MapController
import com.example.mapapp.ui.MapScreen
import com.example.mapapp.ui.theme.MapAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val controller = if (MapConfig.hasApiKey) MapController(this) else null
        controller?.let { lifecycle.addObserver(it) }
        setContent {
            MapAppTheme { MapScreen(controller) }
        }
    }
}
