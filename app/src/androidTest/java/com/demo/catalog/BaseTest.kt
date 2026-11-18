package com.demo.catalog

import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.kaspersky.kaspresso.kaspresso.Kaspresso
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import com.kaspersky.components.composesupport.config.withComposeSupport
import org.junit.After
import org.junit.Rule

/**
 * Базовый класс UI-тестов: Kaspresso + Compose.
 * Репозиторий подменяется фейком, поэтому тесты не зависят от сети.
 */
abstract class BaseTest : TestCase(kaspressoBuilder = Kaspresso.Builder.withComposeSupport()) {

    @get:Rule
    val composeRule = createEmptyComposeRule()

    private var scenario: ActivityScenario<MainActivity>? = null

    protected fun launchApp(repository: FakeProductRepository) {
        val app = ApplicationProvider.getApplicationContext<CatalogApp>()
        app.repository = repository
        app.themeSettings.setDark(false)
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun closeApp() {
        scenario?.close()
    }
}
