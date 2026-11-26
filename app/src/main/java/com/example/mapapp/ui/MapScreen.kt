package com.example.mapapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mapapp.controller.MapController

@Composable
fun MapScreen(controller: MapController?) {
    Surface(modifier = Modifier.fillMaxSize()) {
        if (controller == null) {
            Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Text("Добавьте MAPKIT_API_KEY в local.properties и пересоберите приложение.")
            }
        } else {
            var address by rememberSaveable { mutableStateOf("") }
            val focus = LocalFocusManager.current
            val state = controller.state
            Column(
                modifier = Modifier.fillMaxSize().systemBarsPadding().imePadding().padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Карта", style = MaterialTheme.typography.headlineMedium)
                SearchBar(address, { address = it }, state.isSearching) {
                    focus.clearFocus()
                    controller.search(address)
                }
                state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                // AndroidView встраивает обычный MapView в Compose.
                AndroidView(
                    factory = { controller.mapView },
                    modifier = Modifier.fillMaxWidth().weight(1f).clip(RoundedCornerShape(12.dp))
                )
                Button(
                    onClick = { focus.clearFocus(); address = ""; controller.clear() },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Очистить карту") }
            }
        }
    }
}
