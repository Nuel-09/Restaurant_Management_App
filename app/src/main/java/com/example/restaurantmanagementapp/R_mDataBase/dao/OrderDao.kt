package com.example.restaurantmanagementapp.R_mDataBase.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.restaurantmanagementapp.R_mDataBase.entity.OrderEntity
import com.example.restaurantmanagementapp.R_mDataBase.entity.OrderStatus
import com.example.restaurantmanagementapp.R_mDataBase.dao.BaseDao

@Dao
interface OrderDao : BaseDao<OrderEntity> {
    @Query("SELECT * FROM Orders WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getOrdersByUser(userId: Int): List<OrderEntity>

    @Query("SELECT * FROM Orders ORDER BY createdAt DESC")
    suspend fun getAllOrders(): List<OrderEntity>

    @Query("SELECT * FROM Orders WHERE status = :status ORDER BY createdAt DESC")
    suspend fun getOrdersByStatus(status: String): List<OrderEntity>

    @Query("SELECT * FROM Orders WHERE id = :orderId LIMIT 1")
    suspend fun getOrderById(orderId: Int): OrderEntity?

    @Query("UPDATE Orders SET status = :status, updatedAt = :updatedAt WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: Int, status: String, updatedAt: Long = System.currentTimeMillis())
}
