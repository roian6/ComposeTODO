package com.david0926.simpletodo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    surface = Purple,
    onSurface = Color.White,
    primary = Purple,
    onPrimary = Color.White
)

private val LightColorPalette = lightColors(
    surface = Color.White,
    onSurface = Purple,
    primary = Purple,
    onPrimary = Color.White
)

@Composable
fun SimpleTODOTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}