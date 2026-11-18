package com.demo.catalog.data

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val title: String,
    val description: String = "",
    val price: Double,
    val category: String = "",
    val rating: Double = 0.0,
    val brand: String? = null,
    val thumbnail: String = "",
)

@Serializable
data class ProductsResponse(val products: List<Product>)
