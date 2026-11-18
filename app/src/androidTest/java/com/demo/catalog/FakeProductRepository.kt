package com.demo.catalog

import com.demo.catalog.data.Product
import com.demo.catalog.data.ProductRepository

class FakeProductRepository(
    @Volatile var products: List<Product> = emptyList(),
    @Volatile var error: Throwable? = null,
) : ProductRepository {
    override suspend fun getProducts(): List<Product> = error?.let { throw it } ?: products
    override suspend fun getProduct(id: Int): Product =
        error?.let { throw it } ?: products.first { it.id == id }
}

fun product(id: Int, title: String = "Product $id", rating: Double = 4.0) =
    Product(id = id, title = title, price = id * 10.0, category = "beauty", rating = rating)
