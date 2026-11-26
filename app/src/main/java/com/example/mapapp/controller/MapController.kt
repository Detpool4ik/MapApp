package com.example.mapapp.controller

import android.content.Context
import android.graphics.PointF
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.mapapp.R
import com.example.mapapp.config.MapConfig
import com.example.mapapp.model.MapMarker
import com.example.mapapp.model.MapState
import com.example.mapapp.service.DistanceService
import com.example.mapapp.service.SearchService
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import java.lang.ref.WeakReference
class MapController(context: Context) : DefaultLifecycleObserver, InputListener {
    val mapView = MapView(context)
    private val map = mapView.mapWindow.map
    private val pins = map.mapObjects.addCollection()
    private val lines = map.mapObjects.addCollection()
    private val pinIcon = ImageProvider.fromResource(context, R.drawable.map_pin)
    private val searchService = SearchService()

    var state by mutableStateOf(MapState())
        private set

    init {
        map.addInputListener(WeakReference(this))
        moveCamera(MapConfig.initialPoint, MapConfig.INITIAL_ZOOM)
    }

    // Тап и поиск используют один и тот же метод добавления маркера.
    fun addMarker(marker: MapMarker) {
        pins.addPlacemark().apply {
            geometry = marker.toPoint()
            setIcon(pinIcon)
            setIconStyle(IconStyle().apply { anchor = PointF(0.5f, 1f) })
            zIndex = 10f
        }
        val markers = state.markers + marker
        lines.clear()
        val distance = if (markers.size >= 2) {
            val first = markers[markers.lastIndex - 1]
            val second = markers.last()
            lines.addPolyline(Polyline(listOf(first.toPoint(), second.toPoint()))).apply {
                strokeWidth = 4f
                setStrokeColor(android.graphics.Color.rgb(101, 80, 164))
            }
            DistanceService.calculate(first, second)
        } else null
        state = state.copy(markers = markers, distanceMeters = distance, error = null)
    }

    fun search(address: String) {
        if (address.isBlank()) {
            state = state.copy(error = "Введите адрес")
            return
        }
        state = state.copy(isSearching = true, error = null)
        try {
            searchService.findAddress(
                address.trim(), VisibleRegionUtils.toPolygon(map.visibleRegion),
                onFound = { marker ->
                    addMarker(marker)
                    moveCamera(marker, MapConfig.SEARCH_ZOOM)
                    state = state.copy(isSearching = false)
                },
                onError = { message ->
                    state = state.copy(isSearching = false, error = message)
                }
            )
        } catch (error: Exception) {
            state = state.copy(isSearching = false, error = "Ошибка запуска поиска")
        }
    }

    fun clear() {
        searchService.cancel()
        pins.clear()
        lines.clear()
        state = MapState()
    }

    private fun moveCamera(marker: MapMarker, zoom: Float) {
        map.move(CameraPosition(marker.toPoint(), zoom, 0f, 0f))
    }

    private fun MapMarker.toPoint() = Point(latitude, longitude)

    override fun onMapTap(map: Map, point: Point) {
        addMarker(MapMarker(point.latitude, point.longitude))
    }
    override fun onMapLongTap(map: Map, point: Point) = Unit

    // MapKit и MapView запускаются и останавливаются вместе с Activity.
    override fun onStart(owner: LifecycleOwner) {
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }
    override fun onStop(owner: LifecycleOwner) {
        searchService.cancel()
        state = state.copy(isSearching = false)
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }
    override fun onDestroy(owner: LifecycleOwner) {
        searchService.cancel()
        map.removeInputListener(WeakReference(this))
        owner.lifecycle.removeObserver(this)
    }
}
