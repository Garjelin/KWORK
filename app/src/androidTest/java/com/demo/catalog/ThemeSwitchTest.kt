package com.demo.catalog

import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import org.junit.Rule
import org.junit.Test

class ThemeSwitchTest {
    @get:Rule
    val compose = createEmptyComposeRule()

    @Test
    fun themeSwitch_togglesDarkTheme() {
        val app = ApplicationProvider.getApplicationContext<CatalogApp>()
        app.repository = FakeProductRepository(products = listOf(product(1)))
        app.themeSettings.setDark(false)

        ActivityScenario.launch(MainActivity::class.java).use {
            compose.onNodeWithTag("theme_switch").assertIsOff()
            compose.onNodeWithTag("theme_switch").performClick()
            compose.onNodeWithTag("theme_switch").assertIsOn()
        }
    }
}
