package com.demo.catalog.screens

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen
import com.kaspersky.components.composesupport.core.KNode

/** Page Object: экран со списком товаров. */
class ListScreen(private val provider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<ListScreen>(provider, viewBuilderAction = { hasTestTag("list_root") }) {

    val themeSwitch = KNode(provider) { hasTestTag("theme_switch") }
    val searchField = KNode(provider) { hasTestTag("search_field") }
    val productList = KNode(provider) { hasTestTag("product_list") }
    val emptyResult = KNode(provider) { hasTestTag("empty_result") }
    val offlineBanner = KNode(provider) { hasTestTag("offline_banner") }

    fun product(title: String) = KNode(provider) { hasText(title) }
    fun categoryChip(category: String) = KNode(provider) { hasTestTag("chip_$category") }
}
