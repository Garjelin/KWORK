package com.demo.catalog.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.demo.catalog.CatalogApp

private fun CreationExtras.app() = this[APPLICATION_KEY] as CatalogApp

@Composable
fun CatalogNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            val vm: ListViewModel = viewModel(factory = viewModelFactory {
                initializer { ListViewModel(app().repository) }
            })
            val state by vm.state.collectAsStateWithLifecycle()
            ListScreen(
                state = state,
                onProductClick = { navController.navigate("detail/$it") },
                onRetry = vm::load,
            )
        }
        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType }),
        ) {
            val vm: DetailViewModel = viewModel(factory = viewModelFactory {
                initializer { DetailViewModel(app().repository, createSavedStateHandle()) }
            })
            val state by vm.state.collectAsStateWithLifecycle()
            DetailScreen(
                state = state,
                onBack = { navController.popBackStack() },
                onRetry = vm::load,
            )
        }
    }
}
