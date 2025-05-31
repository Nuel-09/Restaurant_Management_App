package com.example.restaurantmanagementapp.Repository

import com.example.restaurantmanagementapp.R_mDataBase.dao.LeaveRequestDao
import com.example.restaurantmanagementapp.R_mDataBase.entity.LeaveRequestEntity

class LeaveRequestRepository(private val leaveRequestDao: LeaveRequestDao) {
    suspend fun getAllLeaveRequests(): List<LeaveRequestEntity> = leaveRequestDao.getAllLeaveRequests()
    suspend fun getLeaveRequestsForEmployee(employeeId: Int): List<LeaveRequestEntity> = leaveRequestDao.getLeaveRequestsForEmployee(employeeId)
    suspend fun insertLeaveRequest(request: LeaveRequestEntity): Long = leaveRequestDao.insert(request)
    suspend fun updateLeaveRequest(request: LeaveRequestEntity) = leaveRequestDao.update(request)
    suspend fun deleteLeaveRequest(request: LeaveRequestEntity) = leaveRequestDao.delete(request)
}