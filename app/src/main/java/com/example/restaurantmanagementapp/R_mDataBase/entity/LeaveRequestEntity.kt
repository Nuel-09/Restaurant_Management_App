package com.example.restaurantmanagementapp.R_mDataBase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leave_requests")
data class LeaveRequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val employeeId: String,
    val requestDate: Long,
    val leaveStart: Long,
    val leaveEnd: Long,
    val reason: String,
    val status: String // pending, approved, rejected
)