package com.demo.catalog.ui

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

/** Переключатель тёмной темы для верхней панели. */
@Composable
fun ThemeSwitch(darkTheme: Boolean, onChange: (Boolean) -> Unit) {
    Switch(
        checked = darkTheme,
        onCheckedChange = onChange,
        modifier = Modifier
            .testTag("theme_switch")
            .semantics { contentDescription = "Тёмная тема" },
    )
}
