package com.smarttranslator.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF7C9CFF),
    secondary = androidx.compose.ui.graphics.Color(0xFF9AE6B4),
    tertiary = androidx.compose.ui.graphics.Color(0xFFF6BD60),
    background = androidx.compose.ui.graphics.Color(0xFF121826),
    surface = androidx.compose.ui.graphics.Color(0xFF1A2235),
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onBackground = androidx.compose.ui.graphics.Color.White,
    onSurface = androidx.compose.ui.graphics.Color.White
)

private val LightColors = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF3366FF),
    secondary = androidx.compose.ui.graphics.Color(0xFF3ABF9E),
    tertiary = androidx.compose.ui.graphics.Color(0xFFFFB547),
    background = androidx.compose.ui.graphics.Color(0xFFF4F7FF),
    surface = androidx.compose.ui.graphics.Color.White,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    onBackground = androidx.compose.ui.graphics.Color(0xFF1A2235),
    onSurface = androidx.compose.ui.graphics.Color(0xFF1A2235)
)

@Composable
fun SmartTranslatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
