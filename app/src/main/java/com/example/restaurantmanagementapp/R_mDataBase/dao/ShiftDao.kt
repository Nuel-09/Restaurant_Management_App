package com.example.restaurantmanagementapp.R_mDataBase.dao

import androidx.room.*
import com.example.restaurantmanagementapp.R_mDataBase.entity.EmployeeIdName
import com.example.restaurantmanagementapp.R_mDataBase.entity.ShiftEntity
import com.example.restaurantmanagementapp.R_mDataBase.entity.ShiftWithEmployee

@Dao
interface ShiftDao : BaseDao<ShiftEntity> {
    @Query("SELECT * FROM shifts ORDER BY startTime DESC")
    suspend fun getAllShifts(): List<ShiftEntity>

    @Query("""
        SELECT 
            shifts.id,
            shifts.employeeId,
            users.username AS employeeName,
            shifts.startTime,
            shifts.endTime,
            shifts.status,
            shifts.tableNumbers,
            shifts.notes,
            shifts.cancellationReason
        FROM shifts
        INNER JOIN users ON shifts.employeeId = users.username
        WHERE users.role = 'employee'
        ORDER BY shifts.startTime DESC
    """)
    suspend fun getAllShiftsWithEmployee(): List<ShiftWithEmployee>

    @Query("""
        SELECT 
            shifts.id,
            shifts.employeeId,
            users.username AS employeeName,
            shifts.startTime,
            shifts.endTime,
            shifts.status,
            shifts.tableNumbers,
            shifts.notes,
            shifts.cancellationReason
        FROM shifts
        INNER JOIN users ON shifts.employeeId = users.username
        WHERE users.role = 'employee'
          AND shifts.startTime BETWEEN :startMillis AND :endMillis
        ORDER BY shifts.startTime DESC
    """)
    suspend fun getShiftsWithEmployeeByDate(startMillis: Long, endMillis: Long): List<ShiftWithEmployee>

    @Query("SELECT id, username FROM users WHERE role = 'employee'")
    suspend fun getAllEmployees(): List<EmployeeIdName>

    @Query("SELECT * FROM shifts WHERE employeeId = :employeeId ORDER BY startTime DESC")
    suspend fun getShiftsForEmployee(employeeId: String): List<ShiftEntity>

}