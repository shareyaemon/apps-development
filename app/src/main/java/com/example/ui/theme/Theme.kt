package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = NavyPrimary,
    onPrimary = Color.White,
    primaryContainer = SoftSkyBlue,
    onPrimaryContainer = NavyPrimary,
    secondary = SkyBlueAccent,
    onSecondary = NavyPrimary,
    secondaryContainer = SoftSkyBlue,
    onSecondaryContainer = NavyPrimary,
    background = BackgroundLight,
    onBackground = NavyPrimary,
    surface = SurfaceWhite,
    onSurface = NavyPrimary,
    surfaceVariant = SurfaceWhite,
    onSurfaceVariant = SubtitleGray,
    outline = CardBorder
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography,
        content = content
    )
}
