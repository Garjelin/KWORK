package com.demo.catalog.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import com.kaspersky.components.composesupport.core.KNode

/** Page Object: карточка товара. */
class DetailScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<DetailScreen>(semanticsProvider, viewBuilderAction = { hasTestTag("detail_root") }) {

    val title = KNode(semanticsProvider) { hasTestTag("detail_title") }
    val price = KNode(semanticsProvider) { hasTestTag("detail_price") }
    val backButton = KNode(semanticsProvider) { hasTestTag("back_button") }
}
