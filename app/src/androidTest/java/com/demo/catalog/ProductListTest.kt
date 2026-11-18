package com.demo.catalog

import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import com.demo.catalog.screens.ListScreen
import org.junit.Test

class ProductListTest : BaseTest() {

    @Test
    fun listShowsLoadedProducts() = run {
        step("Открываем приложение с тремя товарами") {
            launchApp(FakeProductRepository(products = listOf(product(1, "Red Lipstick"), product(2, "Green Apple"), product(3, "Blue Jeans"))))
        }
        step("Список показывает все товары") {
            onComposeScreen<ListScreen>(composeRule) {
                productList.assertIsDisplayed()
                product("Red Lipstick").assertIsDisplayed()
                product("Green Apple").assertIsDisplayed()
                product("Blue Jeans").assertIsDisplayed()
            }
        }
    }
}
