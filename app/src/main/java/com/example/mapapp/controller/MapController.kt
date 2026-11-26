package com.example.mapapp.controller

import android.content.Context
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
import com.example.mapapp.ui.theme.MapStyle
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.mapview.MapView
import java.lang.ref.WeakReference

class MapController(private val context: Context) : DefaultLifecycleObserver, InputListener {
    val mapView = MapView(context)
    private val map = mapView.mapWindow.map
    private val pins = map.mapObjects.addCollection()
    private val lines = map.mapObjects.addCollection()
    private val labels = map.mapObjects.addCollection()
    private val inputListener = WeakReference<InputListener>(this)
    private val mapStyle = MapStyle(context)
    private val searchService = SearchService(context)

    var state by mutableStateOf(MapState())
        private set

    init {
        map.addInputListener(inputListener)
        moveCamera(MapConfig.initialPoint, MapConfig.INITIAL_ZOOM)
    }

    // Поиск и нажатие на карту используют общий метод добавления точки.
    fun addMarker(marker: MapMarker) {
        state = state.withMarker(marker).copy(error = null)
        redrawMap()
    }

    private fun redrawMap() {
        pins.clear()
        lines.clear()
        labels.clear()

        state.markers.forEach { marker ->
            pins.addPlacemark().apply {
                geometry = marker.toPoint()
                setIcon(mapStyle.pinIcon)
                setIconStyle(mapStyle.markerStyle())
                zIndex = mapStyle.markerZIndex
            }
        }

        state.edges.forEach { (first, second) ->
            drawEdge(first, second)
        }
    }

    private fun drawEdge(first: MapMarker, second: MapMarker) {
        val start = first.toPoint()
        val end = second.toPoint()
        val middle = DistanceService.midpoint(start, end)
        lines.addPolyline(Polyline(listOf(start, middle, end))).apply {
            strokeWidth = mapStyle.lineWidth
            setStrokeColor(mapStyle.lineColor)
        }

        // Текстовая метка находится в середине линии и движется вместе с картой.
        val distance = DistanceService.format(context, DistanceService.calculate(start, end))
        labels.addPlacemark().apply {
            geometry = middle
            zIndex = mapStyle.labelZIndex
            setText(distance, mapStyle.labelStyle())
        }
    }

    fun search(address: String) {
        if (address.isBlank()) {
            state = state.copy(error = context.getString(R.string.error_empty_address))
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
            state = state.copy(isSearching = false, error = context.getString(R.string.error_search_start))
        }
    }

    fun clear() {
        searchService.cancel()
        state = MapState()
        redrawMap()
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
        map.removeInputListener(inputListener)
        owner.lifecycle.removeObserver(this)
    }
}
