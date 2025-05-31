package com.example.restaurantmanagementapp.Activity.AdminActivity.ShiftManagement

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.restaurantmanagementapp.R_mDataBase.entity.EmployeeIdName
import com.example.restaurantmanagementapp.R_mDataBase.entity.ShiftWithEmployee
import com.example.restaurantmanagementapp.ViewModel.ShiftListViewModel
import java.text.SimpleDateFormat
import java.util.*

import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShiftListScreenAdmin(
    viewModel: ShiftListViewModel,
    onBack: () -> Unit // Pass this from your navigation
) {
    val shifts by viewModel.shifts.collectAsState()
    val selectedShifts by viewModel.selectedShifts.collectAsState()
    val showDialog by viewModel.showAddEditDialog.collectAsState()
    val editingShift by viewModel.editingShift.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shifts ") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.Search, contentDescription = "Search by Date")
                    }
                    IconButton(onClick = { viewModel.onAddShift() }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Shift")
                    }
                    IconButton(
                        onClick = { viewModel.onEditSelected() },
                        enabled = selectedShifts.size == 1
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Shift")
                    }
                    IconButton(
                        onClick = { viewModel.onDeleteSelected() },
                        enabled = selectedShifts.isNotEmpty()
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Shift")
                    }
                }
            )
        }
    ) { padding ->
        if (shifts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No shifts available.", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(shifts.size) { idx ->
                    val shift = shifts[idx]
                    ShiftListItemAdmin(
                        shift = shift,
                        isSelected = selectedShifts.contains(shift.id),
                        onClick = { viewModel.onShiftClick(shift.id) }
                    )
                }
            }
        }
    }

    // DatePickerDialog for search
    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedMillis ->
                            viewModel.loadShiftsByDate(selectedMillis)
                        }
                        showDatePicker = false
                    }
                ) { Text("Search") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    ShiftAddEditDialog(
        show = showDialog,
        initialShift = editingShift,
        employees = viewModel.employees.collectAsState().value,
        onDismiss = { viewModel.dismissDialog() },
        onConfirm = { employeeIds, startTime, endTime, status, tableNumbers, notes, id ->
            viewModel.addOrUpdateShift(
                employeeIds = employeeIds,
                startTime = startTime,
                endTime = endTime,
                status = status,
                tableNumbers = tableNumbers,
                notes = notes,
                id = id
            )
        }
    )
}

@Composable
fun ShiftListItemAdmin(
    shift: ShiftWithEmployee,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("EEE, MMM d yyyy HH:mm", Locale.getDefault()) }
    val start = dateFormat.format(Date(shift.startTime))
    val end = dateFormat.format(Date(shift.endTime))
    val statusColor = when (shift.status) {
        "scheduled" -> Color(0xFF1976D2)
        "in_progress" -> Color(0xFFD32F2F)
        "employee-rejected" -> Color(0xFF43A047)
        "completed" -> Color(0xFF616161)
        "cancelled" -> Color(0xFFD32F2F)
        "employee_cancelled" -> Color(0xFFFFA000)
        "missed" -> Color(0xFFB71C1C)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(if (isSelected) MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f) else Color.Transparent)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Employee: ${shift.employeeName} ",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    shift.status.replaceFirstChar { it.uppercase() },
                    color = statusColor,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text("Start: $start", style = MaterialTheme.typography.bodySmall)
            Text("End: $end", style = MaterialTheme.typography.bodySmall)
            if (!shift.tableNumbers.isNullOrBlank()) {
                Text("Tables: ${shift.tableNumbers}", style = MaterialTheme.typography.bodySmall)
            }
            if (!shift.notes.isNullOrBlank()) {
                Text("Notes: ${shift.notes}", style = MaterialTheme.typography.bodySmall)
            }
            if (shift.status == "employee_cancelled" && !shift.cancellationReason.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "Cancelled by Employee: ${shift.cancellationReason}",
                    color = Color(0xFFFFA000),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShiftAddEditDialog(
    show: Boolean,
    initialShift: ShiftWithEmployee?,
    employees: List<EmployeeIdName>,
    onDismiss: () -> Unit,
    onConfirm: (
        employeeIds: List<String>,
        startTime: Long,
        endTime: Long,
        status: String,
        tableNumbers: String?,
        notes: String?,
        id: Int?
    ) -> Unit
) {
    if (!show) return

    // Multi-select state
    val initialSelected = if (initialShift != null) listOf(initialShift.employeeId) else emptyList()
    var selectedEmployeeIds by remember { mutableStateOf(initialSelected) }

    // Date/time state
    var startMillis by remember { mutableStateOf(initialShift?.startTime ?: System.currentTimeMillis()) }
    var endMillis by remember { mutableStateOf(initialShift?.endTime ?: System.currentTimeMillis() + 2 * 60 * 60 * 1000) }

    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }

    // For status, table numbers, notes
    var status by remember { mutableStateOf(initialShift?.status ?: "scheduled") }
    var tableNumbers by remember { mutableStateOf(initialShift?.tableNumbers ?: "") }
    var notes by remember { mutableStateOf(initialShift?.notes ?: "") }

    val statusOptions = listOf("scheduled", "in_progress", "completed", "cancelled", "employee_cancelled", "missed")

    // Dialog states for pickers
    val startDateDialogState = rememberMaterialDialogState()
    val startTimeDialogState = rememberMaterialDialogState()
    val endDateDialogState = rememberMaterialDialogState()
    val endTimeDialogState = rememberMaterialDialogState()

    // Calendar helpers
    fun millisToCalendar(millis: Long): Calendar = Calendar.getInstance().apply { timeInMillis = millis }
    fun setCalendarTime(calendar: Calendar, hour: Int, minute: Int): Calendar {
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(
                        selectedEmployeeIds,
                        startMillis,
                        endMillis,
                        status,
                        tableNumbers.ifBlank { null },
                        notes.ifBlank { null },
                        initialShift?.id
                    )
                },
                enabled = selectedEmployeeIds.isNotEmpty() && startMillis < endMillis
            ) {
                Text(if (initialShift == null) "Add" else "Update")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text(if (initialShift == null) "Add Shift" else "Edit Shift") },
        text = {
            Column {
                // Multi-select dropdown for employees
                var expanded by remember { mutableStateOf(false) }
                OutlinedButton(
                    onClick = { expanded = true },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    val label = if (selectedEmployeeIds.isEmpty()) "Select Employees"
                    else employees.filter { selectedEmployeeIds.contains(it.username) }
                        .joinToString { it.username }
                    Text(label)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    employees.forEach { employee ->
                        val checked = selectedEmployeeIds.contains(employee.username)
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = checked,
                                        onCheckedChange = {
                                            selectedEmployeeIds = if (it) {
                                                selectedEmployeeIds + employee.username
                                            } else {
                                                selectedEmployeeIds - employee.username
                                            }
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(employee.username)
                                }
                            },
                            onClick = {
                                selectedEmployeeIds = if (checked) {
                                    selectedEmployeeIds - employee.username
                                } else {
                                    selectedEmployeeIds + employee.username
                                }
                            }
                        )
                    }
                }

                OutlinedTextField(
                    value = tableNumbers,
                    onValueChange = { tableNumbers = it },
                    label = { Text("Table Numbers (comma-separated)") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    singleLine = false
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Text("Status: ", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.width(8.dp))
                    var statusExpanded by remember { mutableStateOf(false) }
                    Box {
                        Button(onClick = { statusExpanded = true }) {
                            Text(status.replaceFirstChar { it.uppercase() })
                        }
                        DropdownMenu(
                            expanded = statusExpanded,
                            onDismissRequest = { statusExpanded = false }
                        ) {
                            statusOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.replaceFirstChar { it.uppercase() }) },
                                    onClick = {
                                        status = option
                                        statusExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Start Date & Time Pickers
                Text("Start Time", style = MaterialTheme.typography.bodyMedium)
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    OutlinedButton(
                        onClick = { startDateDialogState.show() },
                        modifier = Modifier.weight(1f).padding(end = 4.dp)
                    ) {
                        Text(dateFormat.format(Date(startMillis)))
                    }
                    OutlinedButton(
                        onClick = { startTimeDialogState.show() },
                        modifier = Modifier.weight(1f).padding(start = 4.dp)
                    ) {
                        Text(timeFormat.format(Date(startMillis)))
                    }
                }

                // End Date & Time Pickers
                Text("End Time", style = MaterialTheme.typography.bodyMedium)
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    OutlinedButton(
                        onClick = { endDateDialogState.show() },
                        modifier = Modifier.weight(1f).padding(end = 4.dp)
                    ) {
                        Text(dateFormat.format(Date(endMillis)))
                    }
                    OutlinedButton(
                        onClick = { endTimeDialogState.show() },
                        modifier = Modifier.weight(1f).padding(start = 4.dp)
                    ) {
                        Text(timeFormat.format(Date(endMillis)))
                    }
                }

                // --- Date and Time Picker Dialogs ---

                // Start Date Picker
                MaterialDialog(
                    dialogState = startDateDialogState,
                    buttons = {
                        positiveButton("OK")
                        negativeButton("Cancel")
                    }
                ) {
                    datepicker(
                        initialDate = millisToCalendar(startMillis).time.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(),
                        title = "Pick Start Date"
                    ) { localDate ->
                        val cal = millisToCalendar(startMillis)
                        cal.set(Calendar.YEAR, localDate.year)
                        cal.set(Calendar.MONTH, localDate.monthValue - 1)
                        cal.set(Calendar.DAY_OF_MONTH, localDate.dayOfMonth)
                        startMillis = cal.timeInMillis
                    }
                }

                // Start Time Picker
                MaterialDialog(
                    dialogState = startTimeDialogState,
                    buttons = {
                        positiveButton("OK")
                        negativeButton("Cancel")
                    }
                ) {
                    val cal = millisToCalendar(startMillis)
                    timepicker(
                        initialTime = java.time.LocalTime.of(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)),
                        title = "Pick Start Time",
                        is24HourClock = true
                    ) { time ->
                        val calendar = millisToCalendar(startMillis)
                        setCalendarTime(calendar, time.hour, time.minute)
                        startMillis = calendar.timeInMillis
                    }
                }

                // End Date Picker
                MaterialDialog(
                    dialogState = endDateDialogState,
                    buttons = {
                        positiveButton("OK")
                        negativeButton("Cancel")
                    }
                ) {
                    datepicker(
                        initialDate = millisToCalendar(endMillis).time.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate(),
                        title = "Pick End Date"
                    ) { localDate ->
                        val cal = millisToCalendar(endMillis)
                        cal.set(Calendar.YEAR, localDate.year)
                        cal.set(Calendar.MONTH, localDate.monthValue - 1)
                        cal.set(Calendar.DAY_OF_MONTH, localDate.dayOfMonth)
                        endMillis = cal.timeInMillis
                    }
                }

                // End Time Picker
                MaterialDialog(
                    dialogState = endTimeDialogState,
                    buttons = {
                        positiveButton("OK")
                        negativeButton("Cancel")
                    }
                ) {
                    val cal = millisToCalendar(endMillis)
                    timepicker(
                        initialTime = java.time.LocalTime.of(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)),
                        title = "Pick End Time",
                        is24HourClock = true
                    ) { time ->
                        val calendar = millisToCalendar(endMillis)
                        setCalendarTime(calendar, time.hour, time.minute)
                        endMillis = calendar.timeInMillis
                    }
                }
            }
        }
    )
}
