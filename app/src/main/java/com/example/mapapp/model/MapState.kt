package com.example.mapapp.model

data class MapState(
    val markers: List<MapMarker> = emptyList(),
    val isSearching: Boolean = false,
    val error: String? = null
) {
    // Оставляем только три последние точки.
    fun withMarker(marker: MapMarker): MapState {
        return copy(markers = (markers + marker).takeLast(3))
    }

    // Возвращаем пары точек, между которыми нужно нарисовать линии.
    val edges: List<Pair<MapMarker, MapMarker>>
        get() = when (markers.size) {
            2 -> listOf(markers[0] to markers[1])
            3 -> listOf(
                markers[0] to markers[1],
                markers[1] to markers[2],
                markers[2] to markers[0]
            )
            else -> emptyList()
        }
}
