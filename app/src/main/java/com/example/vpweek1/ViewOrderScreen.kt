package com.example.vpweek1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ViewOrdersScreen(viewModel: RestaurantViewModel, onBack: () -> Unit) {
    val orders by viewModel.orders.collectAsState()
    val menuItems by viewModel.menuItems.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("All Orders", style = MaterialTheme.typography.headlineMedium)

        if (orders.isEmpty()) {
            Text("No orders yet.", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(orders) { order ->
                    OrderCard(order, menuItems)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
    }
}

@Composable
fun OrderCard(order: Order, menuItems: List<MenuItem>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("--- ${order.customerName}'s ORDER ---", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(8.dp))

            order.items.forEachIndexed { index, orderItem ->
                val menuItem = menuItems.find { it.id == orderItem.menuItemId }
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("${index + 1}. ${menuItem?.name ?: "Unknown"} x${orderItem.quantity}", modifier = Modifier.weight(1f))
                    Text("$${orderItem.itemPrice}")
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text("TOTAL:", modifier = Modifier.weight(1f), style = MaterialTheme.typography.headlineSmall)
                Text("$${order.totalPrice}", style = MaterialTheme.typography.headlineSmall)
            }

            Text(
                "Ordered: ${SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date(order.timestamp))}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}