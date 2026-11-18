package com.demo.catalog.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = Color(0xFF2F6B00),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD4FF5E),
    onPrimaryContainer = Color(0xFF0D1512),
    background = Color(0xFFF7FAF2),
    surface = Color(0xFFF7FAF2),
    surfaceContainerHighest = Color(0xFFE4EBD9),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFD4FF5E),
    onPrimary = Color(0xFF1B2A00),
    primaryContainer = Color(0xFF3B5700),
    onPrimaryContainer = Color(0xFFD4FF5E),
    background = Color(0xFF0D1512),
    onBackground = Color(0xFFE3EAE4),
    surface = Color(0xFF0D1512),
    onSurface = Color(0xFFE3EAE4),
    surfaceContainerHighest = Color(0xFF1F2B26),
    onSurfaceVariant = Color(0xFFBFC9C2),
)

@Composable
fun CatalogTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Тёмные значки в строке состояния нужны только на светлой теме
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }
    MaterialTheme(colorScheme = if (darkTheme) DarkColors else LightColors, content = content)
}
