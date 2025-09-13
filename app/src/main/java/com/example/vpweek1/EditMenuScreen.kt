package com.example.vpweek1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun EditMenuScreen(
    viewModel: RestaurantViewModel,
    menuItem: MenuItem?,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf(menuItem?.name ?: "") }
    var description by remember { mutableStateOf(menuItem?.description ?: "") }
    var price by remember { mutableStateOf(menuItem?.price?.toString() ?: "") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    if (menuItem == null) {
        Text("Menu item not found", modifier = Modifier.padding(16.dp))
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Edit Menu Item", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        errorMessage?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val priceValue = price.toDoubleOrNull()
                if (priceValue == null) {
                    errorMessage = "Please enter a valid price"
                    return@Button
                }

                val result = viewModel.updateMenuItem(menuItem.id, name, description, priceValue)
                if (result.isSuccess) {
                    onBack()
                } else {
                    errorMessage = result.exceptionOrNull()?.message
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = name.isNotBlank() && description.isNotBlank() && price.isNotBlank()
        ) {
            Text("Update Item")
        }

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}