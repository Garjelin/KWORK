package com.demo.catalog.data

import kotlinx.serialization.json.Json
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface ProductRepository {
    suspend fun getProducts(): List<Product>
    suspend fun getProduct(id: Int): Product
}

class NetworkProductRepository(private val api: ProductApi) : ProductRepository {
    override suspend fun getProducts(): List<Product> = api.products().products
    override suspend fun getProduct(id: Int): Product = api.product(id)

    companion object {
        fun create(): NetworkProductRepository {
            val json = Json { ignoreUnknownKeys = true }
            val retrofit = Retrofit.Builder()
                .baseUrl("https://dummyjson.com/")
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
            return NetworkProductRepository(retrofit.create(ProductApi::class.java))
        }
    }
}
