package com.example.mapapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private fun appTextStyle(
    size: Int,
    line: Int,
    spacing: Float = 0f,
    weight: FontWeight = FontWeight.Normal
) = TextStyle(
    fontFamily = AppFontFamily,
    fontWeight = weight,
    fontSize = size.sp,
    lineHeight = line.sp,
    letterSpacing = spacing.sp
)

// Все размеры, интервалы и начертания текста интерфейса.
val AppTypography = Typography(
    displayLarge = appTextStyle(57, 64, -0.25f),
    displayMedium = appTextStyle(45, 52),
    displaySmall = appTextStyle(36, 44),
    headlineLarge = appTextStyle(32, 40),
    headlineMedium = appTextStyle(28, 36),
    headlineSmall = appTextStyle(24, 32),
    titleLarge = appTextStyle(22, 28),
    titleMedium = appTextStyle(16, 24, 0.15f, FontWeight.Medium),
    titleSmall = appTextStyle(14, 20, 0.1f, FontWeight.Medium),
    bodyLarge = appTextStyle(16, 24, 0.5f),
    bodyMedium = appTextStyle(14, 20, 0.25f),
    bodySmall = appTextStyle(12, 16, 0.4f),
    labelLarge = appTextStyle(14, 20, 0.1f, FontWeight.Medium),
    labelMedium = appTextStyle(12, 16, 0.5f, FontWeight.Medium),
    labelSmall = appTextStyle(11, 16, 0.5f, FontWeight.Medium)
)
