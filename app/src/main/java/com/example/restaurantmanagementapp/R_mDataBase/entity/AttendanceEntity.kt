package com.example.restaurantmanagementapp.R_mDataBase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance")
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val shiftId: Int,
    val employeeId: String,
    val checkInTime: Long? = null,
    val checkOutTime: Long? = null
)