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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.mapapp.R
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mapapp.controller.MapController

@Composable
fun MapScreen(controller: MapController?) {
    Surface(modifier = Modifier.fillMaxSize()) {
        if (controller == null) {
            Box(Modifier.fillMaxSize().padding(dimensionResource(R.dimen.message_padding)), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.missing_api_key))
            }
        } else {
            var address by rememberSaveable { mutableStateOf("") }
            val focus = LocalFocusManager.current
            val state = controller.state
            Column(
                modifier = Modifier.fillMaxSize().systemBarsPadding().imePadding().padding(dimensionResource(R.dimen.screen_padding)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.screen_spacing))
            ) {
                Text(stringResource(R.string.map_title), style = MaterialTheme.typography.headlineMedium)
                SearchBar(address, { address = it }, state.isSearching) {
                    focus.clearFocus()
                    controller.search(address)
                }
                state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                // AndroidView встраивает обычный MapView в Compose.
                AndroidView(
                    factory = { controller.mapView },
                    modifier = Modifier.fillMaxWidth().weight(1f).clip(RoundedCornerShape(dimensionResource(R.dimen.map_corner_radius)))
                )
                Button(
                    onClick = { focus.clearFocus(); address = ""; controller.clear() },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(stringResource(R.string.clear_map)) }
            }
        }
    }
}
