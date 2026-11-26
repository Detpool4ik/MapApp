package com.example.mapapp.ui

import android.content.Context
import com.example.mapapp.R
import java.util.Locale

// Преобразование расстояния в текст для интерфейса.
object DistanceFormatter {
    fun format(context: Context, meters: Double): String {
        val locale = Locale.forLanguageTag("ru-RU")
        return if (meters < 1000) {
            String.format(locale, context.getString(R.string.distance_meters), meters)
        } else {
            String.format(locale, context.getString(R.string.distance_kilometers), meters / 1000)
        }
    }
}
