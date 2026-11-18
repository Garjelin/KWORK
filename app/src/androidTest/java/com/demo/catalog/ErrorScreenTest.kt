package com.demo.catalog

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class ErrorScreenTest {
    @get:Rule
    val compose = createEmptyComposeRule()

    private fun launchWith(repo: FakeProductRepository): ActivityScenario<MainActivity> {
        ApplicationProvider.getApplicationContext<CatalogApp>().repository = repo
        return ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun networkError_showsErrorScreen_andRetryLoadsList() {
        val repo = FakeProductRepository(error = IOException("no network"))
        launchWith(repo).use {
            compose.waitUntil(5_000) {
                compose.onAllNodesWithTagCount("error_view") > 0
            }
            compose.onNodeWithTag("error_view").assertIsDisplayed()
            compose.onNodeWithText("Повторить").assertIsDisplayed()

            repo.error = null
            repo.products = listOf(product(1), product(2))
            compose.onNodeWithText("Повторить").performClick()

            compose.waitUntil(5_000) { compose.onAllNodesWithTagCount("product_list") > 0 }
            compose.onNodeWithText("Product 1").assertIsDisplayed()
        }
    }

    @Test
    fun emptyResponse_showsErrorScreen() {
        launchWith(FakeProductRepository(products = emptyList())).use {
            compose.waitUntil(5_000) { compose.onAllNodesWithTagCount("error_view") > 0 }
            compose.onNodeWithText("Сервер вернул пустой список товаров.").assertIsDisplayed()
            compose.onNodeWithText("Повторить").assertIsDisplayed()
        }
    }
}

private fun androidx.compose.ui.test.junit4.ComposeTestRule.onAllNodesWithTagCount(tag: String): Int =
    onAllNodes(androidx.compose.ui.test.hasTestTag(tag)).fetchSemanticsNodes().size
