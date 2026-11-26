package com.example.mapapp.model

// Единственный источник данных для интерфейса карты.
data class MapState(
    val markers: List<MapMarker> = emptyList(),
    val distanceMeters: Double? = null,
    val isSearching: Boolean = false,
    val error: String? = null
)
