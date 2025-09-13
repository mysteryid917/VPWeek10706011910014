package com.example.vpweek1

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.system.exitProcess

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantApp() {
    var currentScreen by remember { mutableStateOf("main") }
    var selectedMenuItem by remember { mutableStateOf<MenuItem?>(null) }
    val viewModel: RestaurantViewModel = viewModel()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Restaurant Ordering System") },
                actions = {
                    if (currentScreen != "main") {
                        IconButton(onClick = { currentScreen = "main" }) {
                            Icon(Icons.Default.Home, contentDescription = "Home")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (currentScreen) {
                "main" -> MainScreen(
                    onMakeOrder = { currentScreen = "make_order" },
                    onViewOrders = { currentScreen = "view_orders" },
                    onViewMenu = { currentScreen = "view_menu" },
                    onAddMenu = { currentScreen = "add_menu" },
                    onEditMenu = { item ->
                        selectedMenuItem = item
                        currentScreen = "edit_menu"
                    },
                    onDeleteMenu = { currentScreen = "delete_menu" },
                    onExit = { exitProcess(0) }
                )
                "make_order" -> MakeOrderScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = "main" }
                )
                "view_orders" -> ViewOrdersScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = "main" }
                )
                "view_menu" -> ViewMenuScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = "main" },
                    onEditItem = { item ->
                        selectedMenuItem = item
                        currentScreen = "edit_menu"
                    }
                )
                "add_menu" -> AddMenuScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = "main" }
                )
                "edit_menu" -> EditMenuScreen(
                    viewModel = viewModel,
                    menuItem = selectedMenuItem,
                    onBack = { currentScreen = "view_menu" }
                )
                "delete_menu" -> DeleteMenuScreen(
                    viewModel = viewModel,
                    onBack = { currentScreen = "main" }
                )
            }
        }
    }
}
