package com.demo.catalog.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.catalog.data.Product
import com.demo.catalog.data.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface ListState {
    data object Loading : ListState
    data class Content(
        val products: List<Product>,
        val topTitle: String,
        val query: String = "",
        val category: String? = null,
    ) : ListState {
        val categories: List<String> get() = products.map { it.category }.distinct().sorted()
        val visible: List<Product>
            get() = products.filter {
                (category == null || it.category == category) &&
                    it.title.contains(query.trim(), ignoreCase = true)
            }
    }
    data class Error(val message: String) : ListState
}

class ListViewModel(private val repository: ProductRepository) : ViewModel() {
    private val _state = MutableStateFlow<ListState>(ListState.Loading)
    val state: StateFlow<ListState> = _state

    init {
        load()
    }

    fun onQueryChange(query: String) = _state.update {
        if (it is ListState.Content) it.copy(query = query) else it
    }

    fun onCategorySelect(category: String?) = _state.update {
        if (it is ListState.Content) it.copy(category = category) else it
    }

    fun load() {
        _state.value = ListState.Loading
        viewModelScope.launch {
            _state.value = try {
                val products = repository.getProducts()
                val top = products.maxByOrNull { it.rating } ?: throw EmptyCatalogException()
                ListState.Content(products, top.title)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                ListState.Error(e.toUserMessage())
            }
        }
    }
}
