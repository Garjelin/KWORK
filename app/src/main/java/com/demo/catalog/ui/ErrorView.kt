package com.demo.catalog.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.io.IOException

/** Сервер ответил успешно, но товаров в ответе нет. */
class EmptyCatalogException : Exception("Empty products list")

fun Throwable.toUserMessage(): String = when (this) {
    is IOException -> "Не удалось загрузить данные. Проверьте подключение к интернету."
    is EmptyCatalogException -> "Сервер вернул пустой список товаров."
    else -> "Что-то пошло не так. Попробуйте ещё раз."
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(32.dp).testTag("error_view"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Ошибка", style = MaterialTheme.typography.headlineSmall)
        Text(
            message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp).testTag("error_message"),
        )
        Button(onClick = onRetry, modifier = Modifier.testTag("retry_button")) {
            Text("Повторить")
        }
    }
}
