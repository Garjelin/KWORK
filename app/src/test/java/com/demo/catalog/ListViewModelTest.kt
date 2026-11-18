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

    private fun loadedViewModel(): ListViewModel {
        val items = listOf(
            product(1, "Red Lipstick").copy(category = "beauty"),
            product(2, "Red Apple").copy(category = "groceries"),
            product(3, "Green Apple").copy(category = "groceries"),
        )
        return ListViewModel(FakeProductRepository(products = items))
    }

    private fun ListViewModel.visibleTitles() = (state.value as ListState.Content).visible.map { it.title }

    @Test
    fun `search matches title ignoring case`() {
        val vm = loadedViewModel()
        vm.onQueryChange("  APPLE ")
        assertEquals(listOf("Red Apple", "Green Apple"), vm.visibleTitles())
    }

    @Test
    fun `category filter narrows the list`() {
        val vm = loadedViewModel()
        vm.onCategorySelect("beauty")
        assertEquals(listOf("Red Lipstick"), vm.visibleTitles())
    }

    @Test
    fun `search and category work together and reset with all`() {
        val vm = loadedViewModel()
        vm.onCategorySelect("groceries")
        vm.onQueryChange("red")
        assertEquals(listOf("Red Apple"), vm.visibleTitles())

        vm.onCategorySelect(null)
        vm.onQueryChange("")
        assertEquals(3, vm.visibleTitles().size)
        assertEquals(listOf("beauty", "groceries"), (vm.state.value as ListState.Content).categories)
    }

    @Test
    fun `search without matches gives empty list`() {
        val vm = loadedViewModel()
        vm.onQueryChange("zzz")
        assertTrue(vm.visibleTitles().isEmpty())
    }
}
