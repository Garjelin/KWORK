package com.demo.catalog

import com.demo.catalog.ui.ListState
import com.demo.catalog.ui.ListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class ListViewModelTest {
    @Before
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @After
    fun tearDown() = Dispatchers.resetMain()

    @Test
    fun `network error gives Error state instead of crash`() {
        val vm = ListViewModel(FakeProductRepository(error = IOException("no network")))
        assertTrue(vm.state.value is ListState.Error)
    }

    @Test
    fun `empty response gives Error state instead of crash`() {
        val vm = ListViewModel(FakeProductRepository(products = emptyList()))
        assertTrue(vm.state.value is ListState.Error)
    }

    @Test
    fun `retry after error loads products`() {
        val repo = FakeProductRepository(error = IOException("no network"))
        val vm = ListViewModel(repo)
        repo.error = null
        repo.products = listOf(product(1, rating = 3.0), product(2, "Best", rating = 5.0))

        vm.load()

        val state = vm.state.value as ListState.Content
        assertEquals(2, state.products.size)
        assertEquals("Best", state.topTitle)
    }
}
