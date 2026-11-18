package com.demo.catalog

import com.demo.catalog.screens.ErrorScreen
import com.demo.catalog.screens.ListScreen
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import org.junit.Test
import java.io.IOException

class NetworkErrorTest : BaseTest() {

    @Test
    fun networkError_showsErrorScreen_andRetryLoadsList() = run {
        val repo = FakeProductRepository(error = IOException("no network"))

        step("Открываем приложение без сети") { launchApp(repo) }

        step("Показан экран ошибки с кнопкой «Повторить», приложение не упало") {
            onComposeScreen<ErrorScreen>(composeRule) {
                message.assertIsDisplayed()
                retryButton.assertIsDisplayed()
                retryButton.assertTextEquals("Повторить")
            }
        }
        step("Сеть появилась: «Повторить» загружает список") {
            repo.error = null
            repo.products = listOf(product(1, "Red Lipstick"))
            onComposeScreen<ErrorScreen>(composeRule) { retryButton.performClick() }
            onComposeScreen<ListScreen>(composeRule) { product("Red Lipstick").assertIsDisplayed() }
        }
    }
}
