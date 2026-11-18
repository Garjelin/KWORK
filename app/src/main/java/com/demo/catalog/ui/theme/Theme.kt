package com.demo.catalog.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF2F6B00),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD4FF5E),
    onPrimaryContainer = Color(0xFF0D1512),
    background = Color(0xFFF7FAF2),
    surface = Color(0xFFF7FAF2),
    surfaceContainerHighest = Color(0xFFE4EBD9),
)

@Composable
fun CatalogTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColors, content = content)
}
