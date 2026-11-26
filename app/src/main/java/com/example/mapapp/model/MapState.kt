package com.example.mapapp.model

// Состояние карты: сохраняются только три последние точки.
data class MapState(
    val markers: List<MapMarker> = emptyList(),
    val isSearching: Boolean = false,
    val error: String? = null
) {
    fun withMarker(marker: MapMarker): MapState =
        copy(markers = (markers + marker).takeLast(3))

    // Две точки дают одну сторону, три точки замыкают треугольник.
    val edges: List<Pair<MapMarker, MapMarker>>
        get() = if (markers.size == 3) {
            markers.zipWithNext() + (markers.last() to markers.first())
        } else {
            markers.zipWithNext()
        }
}
