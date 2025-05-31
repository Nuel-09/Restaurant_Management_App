package com.example.restaurantmanagementapp.R_mDataBase.dao

import androidx.room.*
import com.example.restaurantmanagementapp.R_mDataBase.entity.AttendanceEntity

@Dao
interface AttendanceDao: BaseDao<AttendanceEntity> {
    @Query("SELECT * FROM attendance WHERE shiftId = :shiftId")
    suspend fun getAttendanceForShift(shiftId: Int): AttendanceEntity?

    @Query("SELECT * FROM attendance WHERE employeeId = :employeeId ORDER BY checkInTime DESC")
    suspend fun getAttendanceForEmployee(employeeId: Int): List<AttendanceEntity>

}