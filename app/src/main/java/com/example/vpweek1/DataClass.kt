package com.example.vpweek1

data class MenuItem(
    val id: Int,
    var name: String,
    var description: String,
    var price: Double
)

data class Order(
    val id: Int,
    val customerName: String,
    val items: List<OrderItem>,
    val totalPrice: Double,
    val timestamp: Long = System.currentTimeMillis()
)

data class OrderItem(
    val menuItemId: Int,
    val quantity: Int,
    val itemPrice: Double
)