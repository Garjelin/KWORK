package com.demo.catalog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import com.demo.catalog.ui.CatalogNavHost
import com.demo.catalog.ui.theme.CatalogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val settings = (application as CatalogApp).themeSettings
        setContent {
            val userChoice by settings.dark.collectAsState()
            val darkTheme = userChoice ?: isSystemInDarkTheme()
            CatalogTheme(darkTheme = darkTheme) {
                CatalogNavHost(darkTheme = darkTheme, onDarkThemeChange = settings::setDark)
            }
        }
    }
}
