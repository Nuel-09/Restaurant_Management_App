package com.example.restaurantmanagementapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.restaurantmanagementapp.R_mDataBase.entity.EmployeeIdName
import com.example.restaurantmanagementapp.R_mDataBase.entity.ShiftEntity
import com.example.restaurantmanagementapp.R_mDataBase.entity.ShiftWithEmployee
import com.example.restaurantmanagementapp.Repository.ShiftRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShiftListViewModel(
    private val repository: ShiftRepository
) : ViewModel() {

    private val _shifts = MutableStateFlow<List<ShiftWithEmployee>>(emptyList())
    val shifts: StateFlow<List<ShiftWithEmployee>> = _shifts

    private val _selectedShifts = MutableStateFlow<Set<Int>>(emptySet())
    val selectedShifts: StateFlow<Set<Int>> = _selectedShifts

    private val _showAddEditDialog = MutableStateFlow(false)
    val showAddEditDialog: StateFlow<Boolean> = _showAddEditDialog

    private val _editingShift = MutableStateFlow<ShiftWithEmployee?>(null)
    val editingShift: StateFlow<ShiftWithEmployee?> = _editingShift

    private val _employees = MutableStateFlow<List<EmployeeIdName>>(emptyList())
    val employees: StateFlow<List<EmployeeIdName>> = _employees

    init {
        loadShifts()
        loadEmployees()
    }

    fun loadShifts() {
        viewModelScope.launch {
            _shifts.value = repository.getAllShiftsWithEmployee()
        }
    }

    fun loadShiftsByDate(dateMillis: Long) {
        // Compute start and end of day
        val calendar = java.util.Calendar.getInstance().apply {
            timeInMillis = dateMillis
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }
        val startMillis = calendar.timeInMillis
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 23)
        calendar.set(java.util.Calendar.MINUTE, 59)
        calendar.set(java.util.Calendar.SECOND, 59)
        calendar.set(java.util.Calendar.MILLISECOND, 999)
        val endMillis = calendar.timeInMillis

        viewModelScope.launch {
            _shifts.value = repository.getShiftsWithEmployeeByDate(startMillis, endMillis)
        }
    }

    fun loadEmployees() {
        viewModelScope.launch {
            _employees.value = repository.getAllEmployees()
        }
    }


    // Load shifts for a specific employee
    fun loadShiftsForEmployee(employeeId: String) {
        viewModelScope.launch {
            // If you want ShiftWithEmployee, you need a repository method for that.
            // If only ShiftEntity is available, you may need to map it.
            val shiftEntities = repository.getShiftsForEmployee(employeeId)
            // If you want to display with employee info, you may need to map or fetch ShiftWithEmployee.
            // For now, we assume ShiftEntity is enough for employee view.
            _shifts.value = shiftEntities.map {
                ShiftWithEmployee(
                    id = it.id,
                    employeeId = it.employeeId,
                    startTime = it.startTime,
                    endTime = it.endTime,
                    status = it.status,
                    tableNumbers = it.tableNumbers,
                    notes = it.notes,
                    cancellationReason = it.cancellationReason,
                    employeeName = "" // You can fetch employee name if needed
                )
            }
        }
    }


    fun onShiftClick(shiftId: Int) {
        _selectedShifts.value = if (_selectedShifts.value.contains(shiftId)) {
            _selectedShifts.value - shiftId
        } else {
            _selectedShifts.value + shiftId
        }
    }

    fun onAddShift() {
        _editingShift.value = null
        _showAddEditDialog.value = true
    }

    fun onEditSelected() {
        val selected = _selectedShifts.value.firstOrNull()
        val shift = _shifts.value.find { it.id == selected }
        _editingShift.value = shift
        _showAddEditDialog.value = true
    }

    fun onDeleteSelected() {
        viewModelScope.launch {
            val shiftsToDelete = _shifts.value.filter { _selectedShifts.value.contains(it.id) }
            for (shift in shiftsToDelete) {
                repository.deleteShift(
                    ShiftEntity(
                        id = shift.id,
                        employeeId = shift.employeeId,
                        startTime = shift.startTime,
                        endTime = shift.endTime,
                        status = shift.status,
                        tableNumbers = shift.tableNumbers,
                        notes = shift.notes,
                        cancellationReason = shift.cancellationReason
                    )
                )
            }
            _selectedShifts.value = emptySet()
            loadShifts()
        }
    }

    fun addOrUpdateShift(
        employeeIds: List<String>,
        startTime: Long,
        endTime: Long,
        status: String,
        tableNumbers: String?,
        notes: String?,
        id: Int? = null
    ) {
        viewModelScope.launch {
            for (employeeId in employeeIds) {
                if (id == null) {
                    repository.insertShift(
                        ShiftEntity(
                            employeeId = employeeId,
                            startTime = startTime,
                            endTime = endTime,
                            status = status,
                            tableNumbers = tableNumbers,
                            notes = notes
                        )
                    )
                } else {
                    repository.updateShift(
                        ShiftEntity(
                            id = id,
                            employeeId = employeeId,
                            startTime = startTime,
                            endTime = endTime,
                            status = status,
                            tableNumbers = tableNumbers,
                            notes = notes
                        )
                    )
                }
            }
            _showAddEditDialog.value = false
            loadShifts()
        }
    }

    fun dismissDialog() {
        _showAddEditDialog.value = false
    }

    class Factory(
        private val repository: ShiftRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ShiftListViewModel(repository) as T
        }
    }
}