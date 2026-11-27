package com.example.mapapp.service

import com.yandex.mapkit.geometry.Geo
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Segment

object DistanceService {
    // Готовый метод MapKit возвращает расстояние в метрах.
    fun calculate(first: Point, second: Point): Double {
        return Geo.distance(first, second)
    }

    // 0.5 означает середину отрезка между точками.
    fun midpoint(first: Point, second: Point): Point {
        if (first.latitude == second.latitude && first.longitude == second.longitude) {
            return first
        }
        return Geo.pointOnSegmentByFactor(Segment(first, second), 0.5)
    }

    // Переводим метры в километры, если расстояние не меньше 1 км.
    fun format(meters: Double): String {
        return if (meters < 1000) {
            String.format("%.0f м", meters)
        } else {
            String.format("%.2f км", meters / 1000)
        }
    }
}
