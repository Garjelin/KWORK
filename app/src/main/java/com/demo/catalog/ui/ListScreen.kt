package com.demo.catalog.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.demo.catalog.data.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    state: ListState,
    onProductClick: (Int) -> Unit,
    onRetry: () -> Unit,
    onQueryChange: (String) -> Unit,
    onCategorySelect: (String?) -> Unit,
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(
                title = { Text("Каталог") },
                actions = {
                    ThemeSwitch(darkTheme, onDarkThemeChange)
                    Spacer(Modifier.width(8.dp))
                },
            ) },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (state) {
                is ListState.Error -> ErrorView(state.message, onRetry)
                ListState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is ListState.Content -> Column {
                    if (state.offline) OfflineBanner()
                    Text(
                        text = "Хит: ${state.topTitle}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                    SearchBar(state, onQueryChange, onCategorySelect)
                    if (state.visible.isEmpty()) {
                        Box(Modifier.fillMaxSize()) {
                            Text(
                                "Ничего не найдено",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.align(Alignment.Center).testTag("empty_result"),
                            )
                        }
                    } else {
                        ProductList(state.visible, onProductClick)
                    }
                }
            }
        }
    }
}

@Composable
private fun OfflineBanner() {
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        modifier = Modifier.fillMaxWidth().testTag("offline_banner"),
    ) {
        Text(
            "Офлайн · показаны сохранённые данные",
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp),
        )
    }
}

@Composable
private fun SearchBar(
    state: ListState.Content,
    onQueryChange: (String) -> Unit,
    onCategorySelect: (String?) -> Unit,
) {
    OutlinedTextField(
        value = state.query,
        onValueChange = onQueryChange,
        placeholder = { Text("Поиск по названию") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("search_field"),
    )
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.testTag("category_chips"),
    ) {
        item {
            FilterChip(
                selected = state.category == null,
                onClick = { onCategorySelect(null) },
                label = { Text("Все") },
                modifier = Modifier.testTag("chip_all"),
            )
        }
        items(state.categories) { category ->
            FilterChip(
                selected = state.category == category,
                onClick = { onCategorySelect(category) },
                label = { Text(categoryLabel(category)) },
                modifier = Modifier.testTag("chip_$category"),
            )
        }
    }
}

private fun categoryLabel(category: String) =
    category.replace('-', ' ').replaceFirstChar { it.uppercase() }

@Composable
private fun ProductList(products: List<Product>, onClick: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier.testTag("product_list"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(products, key = { it.id }) { product ->
            ProductRow(product, onClick = { onClick(product.id) })
        }
    }
}

@Composable
private fun ProductRow(product: Product, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick).testTag("product_item")) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = product.thumbnail,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .padding(0.dp),
            )
            Column(Modifier.padding(start = 16.dp)) {
                Text(
                    product.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    "$%.2f".format(product.price),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}
