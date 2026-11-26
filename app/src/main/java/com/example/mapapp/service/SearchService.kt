package com.example.mapapp.service

import com.example.mapapp.model.MapMarker
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session
import com.yandex.runtime.Error

// Один поисковый сервис. Храним сессию и слушатель, пока запрос выполняется.
class SearchService {
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
                val point = response.collection.children.asSequence()
                    .mapNotNull { it.obj }
                    .flatMap { it.geometry.asSequence() }
                    .mapNotNull { it.point }
                    .firstOrNull()
                if (point == null) onError("Адрес не найден")
                else onFound(MapMarker(point.latitude, point.longitude))
            }

            override fun onSearchError(error: Error) {
                if (id == requestId) {
                    onError("Не удалось найти адрес. Проверьте интернет и API-ключ.")
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

    fun cancel() {
        requestId++
        session?.cancel()
        session = null
        listener = null
    }
}
