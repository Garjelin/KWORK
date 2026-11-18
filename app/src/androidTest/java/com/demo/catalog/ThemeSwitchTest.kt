package com.demo.catalog

import com.demo.catalog.screens.ListScreen
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import org.junit.Test

class ThemeSwitchTest : BaseTest() {

    @Test
    fun themeSwitch_togglesDarkTheme() = run {
        step("Открываем приложение: светлая тема, переключатель выключен") {
            launchApp(FakeProductRepository(products = listOf(product(1))))
            onComposeScreen<ListScreen>(composeRule) { themeSwitch.assertIsOff() }
        }
        step("Нажимаем переключатель: тёмная тема включена") {
            onComposeScreen<ListScreen>(composeRule) {
                themeSwitch.performClick()
                themeSwitch.assertIsOn()
            }
        }
    }
}
