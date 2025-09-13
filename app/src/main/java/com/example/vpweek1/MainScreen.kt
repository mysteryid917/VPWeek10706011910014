package com.example.vpweek1

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    onMakeOrder: () -> Unit,
    onViewOrders: () -> Unit,
    onViewMenu: () -> Unit,
    onAddMenu: () -> Unit,
    onEditMenu: (MenuItem) -> Unit,
    onDeleteMenu: () -> Unit,
    onExit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Restaurant Management System", style = MaterialTheme.typography.headlineMedium)

        Button(
            onClick = onMakeOrder,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("1. Make Order")
        }

        Button(
            onClick = onViewOrders,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("2. View Orders")
        }

        Button(
            onClick = onViewMenu,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("3. View Menu")
        }

        Button(
            onClick = onAddMenu,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("4. Add Menu")
        }

        Button(
            onClick = onDeleteMenu,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("6. Delete Menu")
        }

        // Exit button (number 7)
        Button(
            onClick = onExit,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("7. Exit")
        }
    }
}