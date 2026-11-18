package com.demo.catalog

import android.app.Application
import com.demo.catalog.data.NetworkProductRepository
import com.demo.catalog.data.ProductRepository

class CatalogApp : Application() {
    /** Репозиторий можно подменить в тестах. */
    lateinit var repository: ProductRepository
    lateinit var themeSettings: ThemeSettings

    override fun onCreate() {
        super.onCreate()
        repository = NetworkProductRepository.create(this)
        themeSettings = ThemeSettings(getSharedPreferences("settings", MODE_PRIVATE))
    }
}
