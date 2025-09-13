package com.example.vpweek1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun MakeOrderScreen(viewModel: RestaurantViewModel, onBack: () -> Unit) {
    var customerName by remember { mutableStateOf("") }
    val orderItems = remember { mutableStateMapOf<Int, Int>() }
    var showDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val menuItems by viewModel.menuItems.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Make New Order", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = customerName,
            onValueChange = { customerName = it },
            label = { Text("Customer Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Select Items:", style = MaterialTheme.typography.headlineSmall)

        if (menuItems.isEmpty()) {
            Text("No menu items available. Please add items first.", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(menuItems) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${item.name} - $${item.price}", modifier = Modifier.weight(1f))

                        // Decrease button (text instead of icon)
                        Button(
                            onClick = {
                                val currentQty = orderItems[item.id] ?: 0
                                if (currentQty > 0) {
                                    orderItems[item.id] = currentQty - 1
                                }
                            },
                            modifier = Modifier.size(40.dp),
                            enabled = (orderItems[item.id] ?: 0) > 0
                        ) {
                            Text("-")
                        }

                        Text(
                            (orderItems[item.id] ?: 0).toString(),
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .width(30.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        // Increase button (text instead of icon)
                        Button(
                            onClick = {
                                orderItems[item.id] = (orderItems[item.id] ?: 0) + 1
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Text("+")
                        }
                    }
                    Divider()
                }
            }
        }

        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display selected items summary
        if (orderItems.any { it.value > 0 }) {
            Text("Order Summary:", style = MaterialTheme.typography.headlineSmall)
            orderItems.filter { it.value > 0 }.forEach { (itemId, quantity) ->
                val menuItem = menuItems.find { it.id == itemId }
                menuItem?.let {
                    Text("• ${it.name} x$quantity - $${it.price * quantity}")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                val result = viewModel.createOrder(customerName, orderItems)
                if (result.isSuccess) {
                    showDialog = true
                } else {
                    errorMessage = result.exceptionOrNull()?.message
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = customerName.isNotBlank() && orderItems.any { it.value > 0 } && menuItems.isNotEmpty()
        ) {
            Text("Place Order")
        }

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Order Placed!") },
            text = { Text("Order has been successfully placed.") },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    onBack()
                }) {
                    Text("OK")
                }
            }
        )
    }
}