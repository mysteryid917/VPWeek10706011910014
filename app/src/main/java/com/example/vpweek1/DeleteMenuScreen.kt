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
fun DeleteMenuScreen(viewModel: RestaurantViewModel, onBack: () -> Unit) {
    val menuItems by viewModel.menuItems.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<MenuItem?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Delete Menu Item", style = MaterialTheme.typography.headlineMedium)

        if (menuItems.isEmpty()) {
            Text("Menu is empty. Nothing to delete.", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(menuItems) { item ->
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
                                onClick = {
                                    itemToDelete = item
                                    showDialog = true
                                },
                                modifier = Modifier.align(Alignment.End),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
    }

    if (showDialog && itemToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Delete") },
            text = { Text("Are you sure you want to delete '${itemToDelete?.name}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        itemToDelete?.let {
                            viewModel.deleteMenuItem(it.id)
                        }
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}