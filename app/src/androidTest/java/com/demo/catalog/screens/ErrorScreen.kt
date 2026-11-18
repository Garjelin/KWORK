package com.demo.catalog.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import com.kaspersky.components.composesupport.core.KNode

/** Page Object: экран ошибки с кнопкой «Повторить». */
class ErrorScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<ErrorScreen>(semanticsProvider, viewBuilderAction = { hasTestTag("error_view") }) {

    val message = KNode(semanticsProvider) { hasTestTag("error_message") }
    val retryButton = KNode(semanticsProvider) { hasTestTag("retry_button") }
}
