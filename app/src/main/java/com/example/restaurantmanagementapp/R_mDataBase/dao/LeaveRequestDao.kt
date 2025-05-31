package com.example.restaurantmanagementapp.R_mDataBase.dao

import androidx.room.*
import com.example.restaurantmanagementapp.R_mDataBase.entity.CategoryEntity
import com.example.restaurantmanagementapp.R_mDataBase.entity.LeaveRequestEntity

@Dao
interface LeaveRequestDao : BaseDao<LeaveRequestEntity> {
    @Query("SELECT * FROM leave_requests ORDER BY requestDate DESC")
    suspend fun getAllLeaveRequests(): List<LeaveRequestEntity>

    @Query("SELECT * FROM leave_requests WHERE employeeId = :employeeId ORDER BY requestDate DESC")
    suspend fun getLeaveRequestsForEmployee(employeeId: Int): List<LeaveRequestEntity>

}