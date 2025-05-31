package com.example.restaurantmanagementapp.R_mDataBase.entity

data class ShiftWithEmployee(
    val id: Int,
    val employeeId: String,
    val employeeName: String,
    val startTime: Long,
    val endTime: Long,
    val status: String,
    val tableNumbers: String?,
    val notes: String?,
    val cancellationReason: String? = null,
    val cancellationTimestamp: Long? = null
)