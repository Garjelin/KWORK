package com.demo.catalog.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.catalog.data.Product
import com.demo.catalog.data.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface ListState {
    data object Loading : ListState
    data class Content(val products: List<Product>, val topTitle: String) : ListState
    data class Error(val message: String) : ListState
}

class ListViewModel(private val repository: ProductRepository) : ViewModel() {
    private val _state = MutableStateFlow<ListState>(ListState.Loading)
    val state: StateFlow<ListState> = _state

    init {
        load()
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
