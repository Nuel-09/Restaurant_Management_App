package com.example.restaurantmanagementapp.Repository

import com.example.restaurantmanagementapp.R_mDataBase.dao.ShiftDao
import com.example.restaurantmanagementapp.R_mDataBase.entity.EmployeeIdName
import com.example.restaurantmanagementapp.R_mDataBase.entity.ShiftEntity
import com.example.restaurantmanagementapp.R_mDataBase.entity.ShiftWithEmployee

class ShiftRepository(private val shiftDao: ShiftDao) {
    suspend fun getAllShifts(): List<ShiftEntity> = shiftDao.getAllShifts()
    suspend fun getShiftsForEmployee(employeeId: String): List<ShiftEntity> = shiftDao.getShiftsForEmployee(employeeId)
    suspend fun getShiftsWithEmployeeByDate(startMillis: Long, endMillis: Long): List<ShiftWithEmployee> =
        shiftDao.getShiftsWithEmployeeByDate(startMillis, endMillis)
    suspend fun getAllShiftsWithEmployee(): List<ShiftWithEmployee> = shiftDao.getAllShiftsWithEmployee()
    suspend fun insertShift(shift: ShiftEntity): Long = shiftDao.insert(shift)
    suspend fun updateShift(shift: ShiftEntity) = shiftDao.update(shift)
    suspend fun deleteShift(shift: ShiftEntity) = shiftDao.delete(shift)
    suspend fun getAllEmployees(): List<EmployeeIdName> = shiftDao.getAllEmployees()

}