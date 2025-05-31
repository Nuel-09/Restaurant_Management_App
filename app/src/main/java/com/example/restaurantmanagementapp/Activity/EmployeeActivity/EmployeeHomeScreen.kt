package com.example.restaurantmanagementapp.Activity.EmployeeActivity

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.R_mDataBase.entity.ShiftEntity
import com.example.restaurantmanagementapp.R_mDataBase.entity.ShiftWithEmployee
import com.example.restaurantmanagementapp.Repository.ShiftRepository
import com.example.restaurantmanagementapp.ViewModel.ShiftListViewModel
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import kotlin.math.max
import kotlin.math.min

data class EmployeeShift(
    val id: Int,
    val date: LocalDate,
    val startTime: String,
    val endTime: String,
    val role: String,
    val rawShiftEntity: ShiftWithEmployee? = null // For passing to details screen
)

@SuppressLint("InvalidColorHexValue")
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EmployeeHomeScreen(
    employeeId: String,
    repository: ShiftRepository,
    onRequestClick: (LocalDate) -> Unit,
    onLogout: () -> Unit = {},
    onQuickNav: (String) -> Unit = {},
    hasNewMessages: Boolean = true // Hardcoded for now
) {

    val viewModel: ShiftListViewModel = viewModel(
        factory = ShiftListViewModel.Factory(repository)
    )

    // Navigation state: null = show list, non-null = show details for that shift
    var selectedShift by remember { mutableStateOf<EmployeeShift?>(null) }

    // Load shifts for this employee when the composable is first shown
    LaunchedEffect(employeeId) {
        viewModel.loadShiftsForEmployee(employeeId)
    }

    // Collect the list of shifts from the ViewModel
    val shiftEntities by viewModel.shifts.collectAsState()
    // Map your ShiftEntity to EmployeeShift for UI
    val shifts = shiftEntities.map { entity ->
        EmployeeShift(
            id = entity.id,
            date = java.time.Instant.ofEpochMilli(entity.startTime)
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate(),
            startTime = java.time.Instant.ofEpochMilli(entity.startTime)
                .atZone(java.time.ZoneId.systemDefault()).toLocalTime().toString().substring(0, 5),
            endTime = java.time.Instant.ofEpochMilli(entity.endTime)
                .atZone(java.time.ZoneId.systemDefault()).toLocalTime().toString().substring(0, 5),
            role = entity.notes ?: "N/A" ,// Replace with actual role if available
            rawShiftEntity = entity
        )
    }


    val tabTitles = listOf("My Shifts", "Leave Request", "Performance", "Announcement")
    val tabIcons = listOf(
        Icons.Default. ListAlt,
        Icons.Default.MailOutline,
        Icons.Default.BarChart,
        Icons.Default.Notifications
    )
    var selectedTab by remember { mutableStateOf(0) }
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Company Logo (hardcoded "GTB")
                        Text(
                            text = "G.T.B",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFF7D95854),
                                    shape = CircleShape
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        // Current page title
                        Text(
                            text = tabTitles[selectedTab],
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            modifier = Modifier.weight(1f)
                        )
                        // Message Icon with notification dot
                        Box(
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(32.dp)
                                .drawBehind {
                                    if (hasNewMessages) {
                                        drawCircle(
                                            color = Color.Red,
                                            radius = 6.dp.toPx(),
                                            center = Offset(
                                                x = size.width - 6.dp.toPx(),
                                                y = 6.dp.toPx()
                                            )
                                        )
                                    }
                                }
                        ) {
                            IconButton(onClick = { /* TODO: Open notifications/messages */ }) {
                                Icon(
                                    imageVector = Icons.Default.Message,
                                    contentDescription = "Notifications",
                                    tint = Color.White
                                )
                            }
                        }
                        // List Icon for dropdown menu
                        Box {
                            IconButton(onClick = { menuExpanded = true }) {
                                Icon(
                                    imageVector = Icons.Default.List,
                                    contentDescription = "Menu",
                                    tint = Color.White
                                )
                            }
                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Logout") },
                                    onClick = {
                                        menuExpanded = false
                                        onLogout()
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Logout,
                                            contentDescription = "Logout"
                                        )
                                    }
                                )
                                // Add more menu items here as needed
                                DropdownMenuItem(
                                    text = { Text("Quick Nav Example") },
                                    onClick = {
                                        menuExpanded = false
                                        onQuickNav("example")
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.ArrowForward,
                                            contentDescription = "Quick Nav"
                                        )
                                    }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF7D95854),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = colorResource(R.color.buttombar),
                contentColor = Color.White
            ) {
                tabTitles.forEachIndexed { index, title ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                imageVector = tabIcons[index],
                                contentDescription = title
                            )
                        },
                        label = { Text(title) },
                        alwaysShowLabel = true,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color.White,
                            indicatorColor = MaterialTheme.colorScheme.secondary,
                            unselectedIconColor = Color.White.copy(alpha = 0.7f),
                            unselectedTextColor = Color.White.copy(alpha = 0.7f)
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFDF6F3))
                .padding(padding)
        ) {

            when {
                // Show shift details if a shift is selected
                selectedTab == 0 && selectedShift != null -> {
                // Show the shift details UI under the top bar, like the shift list
                selectedShift?.rawShiftEntity?.let { shiftEntity ->
                    ShiftDetailsScreenUI(
                        shift = shiftEntity,
                        onBack = { selectedShift = null }
                        )
                    }
                }
                    // Show the shift list tab
                    selectedTab == 0 -> {
                    ShiftListTab(
                        employeeId = employeeId,
                        shifts = shifts,
                        onShiftClick = { shift -> selectedShift = shift },
                        onRequestClick = onRequestClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                    // Other tabs placeholder
                    else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Placeholder for ${tabTitles[selectedTab]}",
                            color = Color.Gray,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShiftListTab(
    employeeId: String,
    shifts: List<EmployeeShift>,
    onShiftClick: (EmployeeShift) -> Unit,
    onRequestClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    weekStartDay: DayOfWeek = DayOfWeek.MONDAY // Change to SUNDAY if needed
) {
    val today = LocalDate.now()
    val currentYear = today.year

    // Find the first day of the first week of the year
    val firstDayOfYear = LocalDate.of(currentYear, 1, 1)
    val firstWeekStart = firstDayOfYear.with(TemporalAdjusters.previousOrSame(weekStartDay))

    // Find the last day of the last week of the year
    val lastDayOfYear = LocalDate.of(currentYear, 12, 31)
    val lastWeekStart = lastDayOfYear.with(TemporalAdjusters.previousOrSame(weekStartDay))

    // Calculate total number of weeks in the year
    val totalWeeks = ((lastWeekStart.toEpochDay() - firstWeekStart.toEpochDay()) / 7 + 1).toInt()

    // State: which week are we showing? 0 = first week, totalWeeks-1 = last week
    var weekIndex by remember { mutableStateOf(
        ((today.toEpochDay() - firstWeekStart.toEpochDay()) / 7).toInt().coerceIn(0, totalWeeks-1)
    )}

    // Calculate the start and end date of the current week
    val weekStart = firstWeekStart.plusWeeks(weekIndex.toLong())
    val weekDates = List(7) { weekStart.plusDays(it.toLong()) }

    // Map dates to shifts for quick lookup
    val shiftMap = shifts.associateBy { it.date }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Week navigation row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = { weekIndex = max(0, weekIndex - 1) },
                enabled = weekIndex > 0
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous week")
            }
            // Calculate if we're on the current week
            val today = LocalDate.now()
            val currentWeekIndex = ((today.toEpochDay() - firstWeekStart.toEpochDay()) / 7).toInt().coerceIn(0, totalWeeks-1)
            val isCurrentWeek = weekIndex == currentWeekIndex

            val weekLabel = "${weekDates.first().format(DateTimeFormatter.ofPattern("MMM d"))} - " +
                    "${weekDates.last().format(DateTimeFormatter.ofPattern("MMM d, yyyy"))}"

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = weekLabel,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                if (!isCurrentWeek) {
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { weekIndex = currentWeekIndex }) {
                        Text("Go to current week", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            IconButton(
                onClick = { weekIndex = min(totalWeeks - 1, weekIndex + 1) },
                enabled = weekIndex < totalWeeks - 1
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next week")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // LazyColumn with Modifier.weight(1f) ensures it fills the remaining space and is scrollable.
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(weekDates) { date ->
                val shift = shiftMap[date]
                ShiftDateItem(
                    date = date,
                    shift = shift,
                    onShiftClick = { shift?.let { onShiftClick(it) } },
                    onRequestClick = { onRequestClick(date) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShiftDateItem(
    date: LocalDate,
    shift: EmployeeShift?,
    onShiftClick: () -> Unit,
    onRequestClick: () -> Unit
) {
    val dayFormatter = DateTimeFormatter.ofPattern("EEE")
    val dateFormatter = DateTimeFormatter.ofPattern("MMM d")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = shift != null, onClick = onShiftClick)
            .background(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date (vertical: day, then date)
            Column(
                modifier = Modifier.width(60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = date.format(dayFormatter),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = date.format(dateFormatter),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            if (shift != null) {
                // Show shift info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${shift.startTime} - ${shift.endTime}",
                        fontSize = 16.sp,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = shift.role,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                Button(
                    onClick = onShiftClick,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("View Details")
                }
            } else {
                // No shift assigned
                Text(
                    text = "No shift assigned",
                    fontSize = 15.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )
                OutlinedButton(
                    onClick = onRequestClick,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Request for shift")
                }
            }
        }
    }
}

// Placeholder ShiftDetailsScreen with top bar and back arrow
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShiftDetailsScreen(
    shift: EmployeeShift,
    onBack: () -> Unit
) {
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
                .background(Color(0xFFFDF6F3)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Shift Details", fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Date: ${shift.date}")
            Text("Time: ${shift.startTime} - ${shift.endTime}")
            Text("Role: ${shift.role}")
            // Add more details as needed
        }
    }
}



