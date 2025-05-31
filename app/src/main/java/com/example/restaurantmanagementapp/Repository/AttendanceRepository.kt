package com.example.restaurantmanagementapp.Repository

import com.example.restaurantmanagementapp.R_mDataBase.dao.AttendanceDao
import com.example.restaurantmanagementapp.R_mDataBase.entity.AttendanceEntity

class AttendanceRepository(private val attendanceDao: AttendanceDao) {
    suspend fun getAttendanceForShift(shiftId: Int): AttendanceEntity? = attendanceDao.getAttendanceForShift(shiftId)
    suspend fun getAttendanceForEmployee(employeeId: Int): List<AttendanceEntity> = attendanceDao.getAttendanceForEmployee(employeeId)
    suspend fun insertAttendance(attendance: AttendanceEntity): Long = attendanceDao.insert(attendance)
    suspend fun updateAttendance(attendance: AttendanceEntity) = attendanceDao.update(attendance)
    suspend fun deleteAttendance(attendance: AttendanceEntity) = attendanceDao.delete(attendance)
}