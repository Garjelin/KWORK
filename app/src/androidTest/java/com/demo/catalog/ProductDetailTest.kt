package com.demo.catalog

import com.demo.catalog.screens.DetailScreen
import com.demo.catalog.screens.ListScreen
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import org.junit.Test

class ProductDetailTest : BaseTest() {

    @Test
    fun tapOnProduct_opensDetails_andBackReturnsToList() = run {
        step("Открываем приложение") {
            launchApp(FakeProductRepository(products = listOf(product(1, "Red Lipstick"), product(2, "Green Apple"))))
        }
        step("Тап по товару открывает карточку") {
            onComposeScreen<ListScreen>(composeRule) { product("Green Apple").performClick() }
            onComposeScreen<DetailScreen>(composeRule) {
                title.assertTextEquals("Green Apple")
                price.assertTextEquals("$20.00")
            }
        }
        step("Кнопка «Назад» возвращает к списку") {
            onComposeScreen<DetailScreen>(composeRule) { backButton.performClick() }
            onComposeScreen<ListScreen>(composeRule) { productList.assertIsDisplayed() }
        }
    }
}
