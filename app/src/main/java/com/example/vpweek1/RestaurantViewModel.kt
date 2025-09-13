package com.example.vpweek1

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RestaurantViewModel : ViewModel() {
    private val _menuItems = MutableStateFlow<List<MenuItem>>(emptyList())
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    private var nextMenuItemId = 1
    private var nextOrderId = 1

    val menuItems: StateFlow<List<MenuItem>> = _menuItems.asStateFlow()
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    // Menu CRUD operations
    fun addMenuItem(name: String, description: String, price: Double): Result<Unit> {
        if (name.isBlank()) return Result.failure(IllegalArgumentException("Name cannot be empty"))
        if (description.isBlank()) return Result.failure(IllegalArgumentException("Description cannot be empty"))
        if (price <= 0) return Result.failure(IllegalArgumentException("Price must be positive"))

        val newItem = MenuItem(nextMenuItemId++, name, description, price)
        _menuItems.value = _menuItems.value + newItem
        return Result.success(Unit)
    }

    fun updateMenuItem(id: Int, name: String, description: String, price: Double): Result<Unit> {
        val currentItems = _menuItems.value
        val itemIndex = currentItems.indexOfFirst { it.id == id }
        if (itemIndex == -1) {
            return Result.failure(IllegalArgumentException("Menu item not found"))
        }

        if (name.isBlank()) return Result.failure(IllegalArgumentException("Name cannot be empty"))
        if (description.isBlank()) return Result.failure(IllegalArgumentException("Description cannot be empty"))
        if (price <= 0) return Result.failure(IllegalArgumentException("Price must be positive"))

        val updatedItem = currentItems[itemIndex].copy(
            name = name,
            description = description,
            price = price
        )

        _menuItems.value = currentItems.toMutableList().apply {
            this[itemIndex] = updatedItem
        }
        return Result.success(Unit)
    }

    fun deleteMenuItem(id: Int): Result<Unit> {
        val item = _menuItems.value.find { it.id == id }
        if (item == null) {
            return Result.failure(IllegalArgumentException("Menu item not found"))
        }
        _menuItems.value = _menuItems.value.filter { it.id != id }
        return Result.success(Unit)
    }

    // Order operations
    fun createOrder(customerName: String, orderItems: Map<Int, Int>): Result<Order> {
        if (customerName.isBlank()) return Result.failure(IllegalArgumentException("Customer name cannot be empty"))
        if (orderItems.isEmpty()) return Result.failure(IllegalArgumentException("Order must contain at least one item"))

        val itemsWithDetails = mutableListOf<OrderItem>()
        var totalPrice = 0.0

        for ((itemId, quantity) in orderItems) {
            val menuItem = _menuItems.value.find { it.id == itemId }
            if (menuItem == null) {
                return Result.failure(IllegalArgumentException("Menu item with ID $itemId not found"))
            }
            if (quantity <= 0) {
                return Result.failure(IllegalArgumentException("Quantity must be positive for ${menuItem.name}"))
            }

            val itemTotal = menuItem.price * quantity
            itemsWithDetails.add(OrderItem(itemId, quantity, itemTotal))
            totalPrice += itemTotal
        }

        val newOrder = Order(nextOrderId++, customerName, itemsWithDetails, totalPrice)
        _orders.value = _orders.value + newOrder
        return Result.success(newOrder)
    }

    fun getMenuItemById(id: Int): MenuItem? {
        return _menuItems.value.find { it.id == id }
    }
}