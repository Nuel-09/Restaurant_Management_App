package com.example.restaurantmanagementapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantmanagementapp.R_mDataBase.entity.OrderEntity
import com.example.restaurantmanagementapp.R_mDataBase.entity.OrderItemEntity
import com.example.restaurantmanagementapp.Repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel(private val repository: OrderRepository) : ViewModel() {
    private val _orders = MutableStateFlow<List<OrderEntity>>(emptyList())
    val orders: StateFlow<List<OrderEntity>> = _orders

    private val _orderItems = MutableStateFlow<List<OrderItemEntity>>(emptyList())
    val orderItems: StateFlow<List<OrderItemEntity>> = _orderItems

    fun loadOrdersByUser(userId: Int) {
        viewModelScope.launch {
            _orders.value = repository.getOrdersByUser(userId)
        }
    }

    fun loadAllOrders() {
        viewModelScope.launch {
            _orders.value = repository.getAllOrders()
        }
    }

    fun loadOrdersByStatus(status: String) {
        viewModelScope.launch {
            _orders.value = repository.getOrdersByStatus(status)
        }
    }

    fun loadOrderItems(orderId: Int) {
        viewModelScope.launch {
            _orderItems.value = repository.getOrderItemsByOrderId(orderId)
        }
    }

    fun insertOrder(order: OrderEntity, items: List<OrderItemEntity>) {
        viewModelScope.launch {
            val orderId = repository.insertOrder(order).toInt()
            items.forEach { item ->
                repository.insertOrderItem(item.copy(orderId = orderId))
            }
            loadOrdersByUser(order.userId)
        }
    }

    fun updateOrderStatus(orderId: Int, status: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, status)
            loadAllOrders()
        }
    }
}
