package com.example.restaurantmanagementapp.Repository

import com.example.restaurantmanagementapp.R_mDataBase.dao.OrderDao
import com.example.restaurantmanagementapp.R_mDataBase.dao.OrderItemDao
import com.example.restaurantmanagementapp.R_mDataBase.entity.OrderEntity
import com.example.restaurantmanagementapp.R_mDataBase.entity.OrderItemEntity

class OrderRepository(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao
) {
    suspend fun insertOrder(order: OrderEntity): Long = orderDao.insert(order)
    suspend fun updateOrder(order: OrderEntity) = orderDao.update(order)
    suspend fun getOrdersByUser(userId: Int) = orderDao.getOrdersByUser(userId)
    suspend fun getAllOrders() = orderDao.getAllOrders()
    suspend fun getOrdersByStatus(status: String) = orderDao.getOrdersByStatus(status)
    suspend fun getOrderById(orderId: Int) = orderDao.getOrderById(orderId)
    suspend fun updateOrderStatus(orderId: Int, status: String) = orderDao.updateOrderStatus(orderId, status)

    suspend fun insertOrderItem(orderItem: OrderItemEntity): Long = orderItemDao.insert(orderItem)
    suspend fun getOrderItemsByOrderId(orderId: Int) = orderItemDao.getOrderItemsByOrderId(orderId)
    suspend fun deleteOrderItemsByOrderId(orderId: Int) = orderItemDao.deleteOrderItemsByOrderId(orderId)
}
