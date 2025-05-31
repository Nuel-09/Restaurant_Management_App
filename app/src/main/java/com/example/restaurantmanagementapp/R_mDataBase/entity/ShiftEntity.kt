package com.example.restaurantmanagementapp.R_mDataBase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shifts")
data class ShiftEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val employeeId: String,
    val startTime: Long,
    val endTime: Long,
    val status: String, // scheduled, in_progress, completed, cancelled, employee_cancelled, missed
    val cancellationReason: String? = null,
    val cancellationTimestamp: Long? = null,
    val tableNumbers: String? = null, // comma-separated for waiters
    val notes: String? = null // for chefs or general notes
)