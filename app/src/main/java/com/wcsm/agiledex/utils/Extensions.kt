package com.wcsm.agiledex.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlin.math.min
import android.graphics.Color as AndroidColor

fun Color.toVibrantColor(): Color {
    val hsv = FloatArray(3)
    AndroidColor.colorToHSV(this.toArgb(), hsv)

    hsv[1] = min(hsv[1] * 1.2f, 1.0f)
    hsv[2] = min(hsv[2] * 1.2f, 1.0f)

    return Color(AndroidColor.HSVToColor(hsv))
}