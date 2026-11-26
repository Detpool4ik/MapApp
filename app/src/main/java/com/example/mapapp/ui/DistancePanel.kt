package com.example.mapapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mapapp.model.MapState
import com.example.mapapp.service.DistanceService

@Composable
fun DistancePanel(state: MapState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Расстояние между последними точками", style = MaterialTheme.typography.bodyMedium)
            Text(state.distanceMeters?.let(DistanceService::format) ?: "—",
                style = MaterialTheme.typography.headlineSmall)
            Text("Маркеров: ${state.markers.size} • По прямой", style = MaterialTheme.typography.bodySmall)
            if (state.markers.size < 2) Text("Найдите адрес или нажмите на карту", style = MaterialTheme.typography.bodySmall)
        }
    }
}
