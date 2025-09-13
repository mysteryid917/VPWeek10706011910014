package com.example.vpweek1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ViewMenuScreen(
    viewModel: RestaurantViewModel,
    onBack: () -> Unit,
    onEditItem: (MenuItem) -> Unit
) {
    val menuItems by viewModel.menuItems.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Current Menu", style = MaterialTheme.typography.headlineMedium)

        if (menuItems.isEmpty()) {
            Text("Menu is empty.", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(menuItems) { item ->
                    MenuItemCard(item, onEdit = { onEditItem(item) })
                }
            }
        }

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
    }
}

@Composable
fun MenuItemCard(item: MenuItem, onEdit: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(item.name, style = MaterialTheme.typography.headlineSmall)
            Text(item.description, style = MaterialTheme.typography.bodyMedium)
            Text("$${item.price}", style = MaterialTheme.typography.titleMedium)
            Button(
                onClick = onEdit,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Edit")
            }
        }
    }
}