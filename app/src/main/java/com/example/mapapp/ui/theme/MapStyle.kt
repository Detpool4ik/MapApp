package com.example.mapapp.ui.theme

import android.content.Context
import android.graphics.PointF
import com.example.mapapp.R
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.TextStyle
import com.yandex.runtime.image.ImageProvider

// Оформление объектов MapKit, отдельно от управления картой.
class MapStyle(context: Context) {
    private val resources = context.resources
    private fun dp(id: Int): Float =
        resources.getDimension(id) / resources.displayMetrics.density

    val lineColor = context.getColor(R.color.map_line)
    val lineWidth = dp(R.dimen.map_line_width)
    val markerZIndex = 10f
    val labelZIndex = 20f
    val pinIcon = ImageProvider.fromResource(context, R.drawable.map_pin)

    fun markerStyle() = IconStyle().apply {
        anchor = PointF(0.5f, 1f)
    }

    private val labelSize = dp(R.dimen.map_label_size)
    private val labelColor = context.getColor(R.color.map_label_text)
    private val outlineWidth = dp(R.dimen.map_label_outline_width)
    private val outlineColor = context.getColor(R.color.map_label_outline)
    private val labelOffset = dp(R.dimen.map_label_offset)

    fun labelStyle() = TextStyle(
        labelSize, labelColor, outlineWidth, outlineColor,
        TextStyle.Placement.CENTER, labelOffset, false, false
    )
}
