package com.example.restaurantmanagementapp.R_mDataBase.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.restaurantmanagementapp.R_mDataBase.entity.OrderItemEntity
import com.example.restaurantmanagementapp.R_mDataBase.dao.BaseDao

@Dao
interface OrderItemDao : BaseDao<OrderItemEntity> {
    @Query("SELECT * FROM OrderItems WHERE orderId = :orderId")
    suspend fun getOrderItemsByOrderId(orderId: Int): List<OrderItemEntity>

    @Query("DELETE FROM OrderItems WHERE orderId = :orderId")
    suspend fun deleteOrderItemsByOrderId(orderId: Int)
}
