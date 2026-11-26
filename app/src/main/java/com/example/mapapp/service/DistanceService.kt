package com.example.mapapp.service

import com.example.mapapp.model.MapMarker
import java.util.Locale
import kotlin.math.*

object DistanceService {
    private const val EARTH_RADIUS = 6_371_000.0

    // Формула гаверсинусов: расстояние по поверхности Земли.
    fun calculate(first: MapMarker, second: MapMarker): Double {
        val lat1 = Math.toRadians(first.latitude)
        val lat2 = Math.toRadians(second.latitude)
        val dLat = lat2 - lat1
        val dLon = Math.toRadians(second.longitude - first.longitude)
        val a = sin(dLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)
        return 2 * EARTH_RADIUS * asin(sqrt(a.coerceIn(0.0, 1.0)))
    }

    // Середина отрезка в проекции Меркатора, с учётом перехода через 180°.
    fun midpoint(first: MapMarker, second: MapMarker): MapMarker {
        fun mercatorY(latitude: Double): Double {
            val phi = Math.toRadians(latitude.coerceIn(-85.0, 85.0))
            return asinh(tan(phi))
        }
        val y = (mercatorY(first.latitude) + mercatorY(second.latitude)) / 2
        val latitude = Math.toDegrees(atan(sinh(y)))
        val delta = (second.longitude - first.longitude + 540.0) % 360.0 - 180.0
        val longitude = (first.longitude + delta / 2 + 540.0) % 360.0 - 180.0
        return MapMarker(latitude, longitude)
    }

    fun format(meters: Double): String = if (meters < 1000) {
        String.format(Locale("ru", "RU"), "%.0f м", meters)
    } else {
        String.format(Locale("ru", "RU"), "%.2f км", meters / 1000)
    }
}
