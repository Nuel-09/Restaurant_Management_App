package com.example.restaurantmanagementapp.Activity.EmployeeActivity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurantmanagementapp.R_mDataBase.entity.ShiftEntity
import com.example.restaurantmanagementapp.R_mDataBase.entity.ShiftWithEmployee
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShiftDetailsScreenUI(
    shift: ShiftWithEmployee,
    onBack: () -> Unit = {},
    // Optionally, you can pass in computed pay info, or compute here if you have the logic
    plannedStart: String = formatTime(shift.startTime),
    plannedEnd: String = formatTime(shift.endTime),
    actualStart: String = "--",
    actualEnd: String = "--",
    actualDuration: String = "--",
    breakDuration: String = "--",
    totalHours: String = "--",
    totalPay: String = "--",
    payPerHour: String = "--"
) {
    var shiftStatus by remember { mutableStateOf<ShiftStatus?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text("Home Screen") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFFDF6F3))
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Shift Info Section
            Text("Shift Details", fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(16.dp))
            ShiftInfoSection(
                shiftDate = formatDate(shift.startTime),
                shiftStart = formatTime(shift.startTime),
                shiftEnd = formatTime(shift.endTime),
                tableNumber = shift.tableNumbers ?: "N/A",
                Notes = shift.notes ?: "N/A",
                location = "Main Hall" // You can replace with actual location if available
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Accept/Decline Buttons or Status
            if (shiftStatus == null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { shiftStatus = ShiftStatus.ACCEPTED },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Accept", color = Color.White)
                    }
                    Button(
                        onClick = { shiftStatus = ShiftStatus.DECLINED },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                    ) {
                        Text("Decline", color = Color.White)
                    }
                }
            } else {
                val statusText = when (shiftStatus) {
                    ShiftStatus.ACCEPTED -> "YOU ACCEPTED"
                    ShiftStatus.DECLINED -> "YOU DECLINED"
                    else -> ""
                }
                val statusColor = when (shiftStatus) {
                    ShiftStatus.ACCEPTED -> Color(0xFF4CAF50)
                    ShiftStatus.DECLINED -> Color(0xFFF44336)
                    else -> Color.Gray
                }
                Text(
                    text = statusText,
                    color = statusColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Pay Calculation Section
            PayCalculationSection(
                plannedStart = plannedStart,
                plannedEnd = plannedEnd,
                actualStart = actualStart,
                actualEnd = actualEnd,
                actualDuration = actualDuration,
                breakDuration = breakDuration,
                totalHours = totalHours,
                totalPay = totalPay,
                payPerHour = payPerHour
            )
        }
    }
}

@Composable
fun ShiftInfoSection(
    shiftDate: String,
    shiftStart: String,
    shiftEnd: String,
    tableNumber: String,
    Notes: String,
    location: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        InfoRow(label = "Date", value = shiftDate)
        InfoRow(label = "Time", value = "$shiftStart - $shiftEnd")
        InfoRow(label = "Notes", value = Notes)
        InfoRow(label = "Assigned Table", value = tableNumber)
        InfoRow(label = "Location", value = location)
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.SemiBold)
        Text(value)
    }
}

@Composable
fun PayCalculationSection(
    plannedStart: String,
    plannedEnd: String,
    actualStart: String,
    actualEnd: String,
    actualDuration: String,
    breakDuration: String,
    totalHours: String,
    totalPay: String,
    payPerHour: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Text("Pay calculation", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        // Table header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("", modifier = Modifier.weight(1f))
            Text("Start", modifier = Modifier.weight(1f))
            Text("Stop", modifier = Modifier.weight(1f))
            Text("Duration", modifier = Modifier.weight(1f))
            Text("Value", modifier = Modifier.weight(1f))
        }
        Divider(modifier = Modifier.padding(vertical = 4.dp))
        // Planned row
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Planned:", modifier = Modifier.weight(1f))
            Text(plannedStart, modifier = Modifier.weight(1f))
            Text(plannedEnd, modifier = Modifier.weight(1f))
            Text("--", modifier = Modifier.weight(1f))
            Text("--", modifier = Modifier.weight(1f))
        }
        // Actual row
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Actual:", modifier = Modifier.weight(1f))
            Text(actualStart, modifier = Modifier.weight(1f))
            Text(actualEnd, modifier = Modifier.weight(1f))
            Text(actualDuration, modifier = Modifier.weight(1f))
            Text("--", modifier = Modifier.weight(1f))
        }
        // Breaks row
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Breaks:", modifier = Modifier.weight(1f))
            Text("--", modifier = Modifier.weight(1f))
            Text("--", modifier = Modifier.weight(1f))
            Text(breakDuration, modifier = Modifier.weight(1f))
            Text("--", modifier = Modifier.weight(1f))
        }
        // Totals row
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Totals:", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            Text("", modifier = Modifier.weight(1f))
            Text("", modifier = Modifier.weight(1f))
            Text(totalHours, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            Text(totalPay, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Pay per hour
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Pay to you (per hour):", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(3f))
            Text(payPerHour, fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
        }
    }
}

enum class ShiftStatus { ACCEPTED, DECLINED }

// Helper functions for formatting
@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(epochMillis: Long): String {
    val localDate = Instant.ofEpochMilli(epochMillis)
        .atZone(ZoneId.systemDefault()).toLocalDate()
    return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTime(epochMillis: Long): String {
    val localTime = Instant.ofEpochMilli(epochMillis)
        .atZone(ZoneId.systemDefault()).toLocalTime()
    return localTime.toString().substring(0, 5)
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewShiftDetailsScreenUI() {
    val fakeShift = ShiftWithEmployee(
        id = 1,
        employeeId = "emp1",
        startTime = System.currentTimeMillis(),
        endTime = System.currentTimeMillis() + 8 * 60 * 60 * 1000,
        status = "scheduled",
        cancellationReason = null,
        cancellationTimestamp = null,
        tableNumbers = "Go to table 6",
        employeeName = "",
        notes = "Cook with salt and butter"
    )
    ShiftDetailsScreenUI(
        shift = fakeShift,
        plannedStart = "22:00",
        plannedEnd = "06:10",
        actualStart = "22:00",
        actualEnd = "06:00",
        actualDuration = "8 hours",
        breakDuration = "1 hour",
        totalHours = "7 hours",
        totalPay = "£105.77",
        payPerHour = "£15.11"
    )
}
