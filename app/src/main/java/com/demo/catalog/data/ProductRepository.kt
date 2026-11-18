package com.demo.catalog.data

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

/** Список товаров и признак того, что он взят из кэша (нет сети). */
data class Catalog(val products: List<Product>, val offline: Boolean = false)

interface ProductRepository {
    suspend fun getProducts(): Catalog
    suspend fun getProduct(id: Int): Product
}

class NetworkProductRepository(
    private val api: ProductApi,
    private val dao: ProductDao,
) : ProductRepository {

    override suspend fun getProducts(): Catalog = try {
        val products = api.products().products
        if (products.isNotEmpty()) dao.replaceAll(products.map { it.toEntity() })
        Catalog(products)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        val cached = dao.getAll()
        if (cached.isEmpty()) throw e
        Catalog(cached.map { it.toProduct() }, offline = true)
    }

    override suspend fun getProduct(id: Int): Product = try {
        api.product(id)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        dao.getById(id)?.toProduct() ?: throw e
    }

    companion object {
        fun create(context: Context): NetworkProductRepository {
            val json = Json { ignoreUnknownKeys = true }
            val retrofit = Retrofit.Builder()
                .baseUrl("https://dummyjson.com/")
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
            val db = Room.databaseBuilder(context, ProductDatabase::class.java, "catalog.db").build()
            return NetworkProductRepository(retrofit.create(ProductApi::class.java), db.productDao())
        }
    }
}
