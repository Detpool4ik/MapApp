package com.example.mapapp.service

import android.content.Context
import com.example.mapapp.R
import com.example.mapapp.model.MapMarker
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session
import com.yandex.runtime.Error

// Один поисковый сервис. Храним сессию и слушатель, пока запрос выполняется.
class SearchService(context: Context) {
    private val resources = context.resources
    private val manager = SearchFactory.getInstance()
        .createSearchManager(SearchManagerType.ONLINE)
    private var session: Session? = null
    private var listener: Session.SearchListener? = null
    private var requestId = 0

    fun findAddress(
        address: String,
        region: Geometry,
        onFound: (MapMarker) -> Unit,
        onError: (String) -> Unit
    ) {
        cancel()
        val id = requestId
        val callback = object : Session.SearchListener {
            override fun onSearchResponse(response: Response) {
                if (id != requestId) return
                val point = firstPoint(response)
                if (point == null) onError(resources.getString(R.string.error_address_not_found))
                else onFound(MapMarker(point.latitude, point.longitude))
            }

            override fun onSearchError(error: Error) {
                if (id == requestId) {
                    onError(resources.getString(R.string.error_search_failed))
                }
            }
        }
        listener = callback
        val options = SearchOptions().apply {
            searchTypes = SearchType.GEO.value
            resultPageSize = 10
        }
        session = manager.submit(address, region, options, callback)
    }

    // Берём координаты первого подходящего результата поиска.
    private fun firstPoint(response: Response): Point? {
        for (child in response.collection.children) {
            val result = child.obj ?: continue
            for (geometry in result.geometry) {
                val point = geometry.point
                if (point != null) return point
            }
        }
        return null
    }

    fun cancel() {
        requestId++
        session?.cancel()
        session = null
        listener = null
    }
}
