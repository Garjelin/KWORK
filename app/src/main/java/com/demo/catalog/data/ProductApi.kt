package com.demo.catalog.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {
    @GET("products")
    suspend fun products(@Query("limit") limit: Int = 100): ProductsResponse

    @GET("products/{id}")
    suspend fun product(@Path("id") id: Int): Product
}
