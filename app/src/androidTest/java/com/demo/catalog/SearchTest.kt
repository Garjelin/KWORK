package com.demo.catalog

import com.demo.catalog.screens.ListScreen
import io.github.kakaocup.compose.node.element.ComposeScreen.Companion.onComposeScreen
import org.junit.Test

class SearchTest : BaseTest() {

    private val products = listOf(
        product(1, "Red Lipstick").copy(category = "beauty"),
        product(2, "Red Apple").copy(category = "groceries"),
        product(3, "Green Apple").copy(category = "groceries"),
    )

    @Test
    fun searchAndCategoryFilterNarrowTheList() = run {
        step("Открываем приложение") { launchApp(FakeProductRepository(products = products)) }

        step("Поиск «apple» оставляет только яблоки") {
            onComposeScreen<ListScreen>(composeRule) {
                searchField.performTextInput("apple")
                product("Red Apple").assertIsDisplayed()
                product("Green Apple").assertIsDisplayed()
                product("Red Lipstick").assertDoesNotExist()
            }
        }
        step("Фильтр по категории «beauty» вместе с поиском «red»") {
            onComposeScreen<ListScreen>(composeRule) {
                searchField.performTextReplacement("red")
                categoryChip("beauty").performClick()
                product("Red Lipstick").assertIsDisplayed()
                product("Red Apple").assertDoesNotExist()
            }
        }
        step("Если ничего не найдено, показываем сообщение") {
            onComposeScreen<ListScreen>(composeRule) {
                searchField.performTextReplacement("zzz")
                emptyResult.assertIsDisplayed()
            }
        }
    }
}
