package com.example.mapapp.config

import android.content.Context
import com.example.mapapp.BuildConfig
import com.example.mapapp.model.MapMarker
import com.yandex.mapkit.MapKitFactory

object MapConfig {
    // Начальная позиция карты, не местоположение пользователя.
    val initialPoint = MapMarker(55.7558, 37.6176)
    const val INITIAL_ZOOM = 11f
    const val SEARCH_ZOOM = 16f
    val hasApiKey: Boolean get() = BuildConfig.MAPKIT_API_KEY.isNotBlank()

    fun initialize(context: Context) {
        if (hasApiKey) {
            MapKitFactory.setApiKey(BuildConfig.MAPKIT_API_KEY)
            MapKitFactory.initialize(context)
        }
    }
}
