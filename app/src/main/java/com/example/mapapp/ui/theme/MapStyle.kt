package com.example.mapapp.ui.theme

import android.content.Context
import android.graphics.PointF
import androidx.compose.ui.graphics.toArgb
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.TextStyle
import com.yandex.runtime.image.ImageProvider

// Настройки оформления объектов карты.
class MapStyle(context: Context) {
    val lineColor = MapLineColor.toArgb()
    val lineWidth = 4f
    val markerZIndex = 10f
    val labelZIndex = 20f
    val pinIcon = ImageProvider.fromAsset(context, "map_pin.png")

    fun markerStyle() = IconStyle().apply {
        anchor = PointF(0.5f, 1f)
    }

    fun labelStyle() = TextStyle(
        14f, MapLabelColor.toArgb(), 2f, MapLabelOutlineColor.toArgb(),
        TextStyle.Placement.CENTER, 0f, false, false
    )
}
