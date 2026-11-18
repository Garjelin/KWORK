package com.demo.catalog

import com.demo.catalog.data.NetworkProductRepository
import com.demo.catalog.data.Product
import com.demo.catalog.data.ProductApi
import com.demo.catalog.data.ProductDao
import com.demo.catalog.data.ProductEntity
import com.demo.catalog.data.ProductsResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.io.IOException

class NetworkProductRepositoryTest {

    private class FakeApi(var products: List<Product> = emptyList(), var failure: Exception? = null) : ProductApi {
        override suspend fun products(limit: Int) = failure?.let { throw it } ?: ProductsResponse(products)
        override suspend fun product(id: Int) = failure?.let { throw it } ?: products.first { it.id == id }
    }

    private class InMemoryDao : ProductDao() {
        var rows: List<ProductEntity> = emptyList()
        override suspend fun getAll() = rows
        override suspend fun getById(id: Int) = rows.firstOrNull { it.id == id }
        override suspend fun insertAll(items: List<ProductEntity>) { rows = items }
        override suspend fun deleteAll() { rows = emptyList() }
        override suspend fun replaceAll(items: List<ProductEntity>) { rows = items }
    }

    @Test
    fun `online load returns network data and fills cache`() = runTest {
        val dao = InMemoryDao()
        val repo = NetworkProductRepository(FakeApi(listOf(product(1), product(2))), dao)

        val catalog = repo.getProducts()

        assertFalse(catalog.offline)
        assertEquals(2, catalog.products.size)
        assertEquals(2, dao.rows.size)
    }

    @Test
    fun `without network the list comes from cache and is marked offline`() = runTest {
        val dao = InMemoryDao()
        val api = FakeApi(listOf(product(1), product(2)))
        val repo = NetworkProductRepository(api, dao)
        repo.getProducts()

        api.failure = IOException("no network")
        val catalog = repo.getProducts()

        assertTrue(catalog.offline)
        assertEquals(listOf("Product 1", "Product 2"), catalog.products.map { it.title })
        assertEquals("Product 2", repo.getProduct(2).title)
    }

    @Test
    fun `without network and empty cache the error is propagated`() = runTest {
        val repo = NetworkProductRepository(FakeApi(failure = IOException("no network")), InMemoryDao())
        try {
            repo.getProducts()
            fail("expected IOException")
        } catch (e: IOException) {
            // ожидаемо: дальше ViewModel покажет экран ошибки
        }
    }
}
