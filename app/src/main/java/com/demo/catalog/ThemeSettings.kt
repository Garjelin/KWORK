package com.demo.catalog

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** Выбор темы пользователем. null — следовать системной теме. */
class ThemeSettings(private val prefs: SharedPreferences) {
    private val _dark = MutableStateFlow(
        if (prefs.contains(KEY)) prefs.getBoolean(KEY, false) else null,
    )
    val dark: StateFlow<Boolean?> = _dark

    fun setDark(value: Boolean) {
        prefs.edit().putBoolean(KEY, value).apply()
        _dark.value = value
    }

    private companion object {
        const val KEY = "dark_theme"
    }
}
