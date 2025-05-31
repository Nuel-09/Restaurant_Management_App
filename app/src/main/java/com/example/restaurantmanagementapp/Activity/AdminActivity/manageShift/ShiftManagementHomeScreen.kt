package com.example.restaurantmanagementapp.Activity.AdminActivity.manageShift

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restaurantmanagementapp.ViewModel.ShiftListViewModel
import com.example.restaurantmanagementapp.Activity.AdminActivity.ShiftManagement.ShiftListScreenAdmin
import com.example.restaurantmanagementapp.R


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShiftManagementHomeScreen(
    onBack: () -> Unit,
    shiftListViewModelFactory: ShiftListViewModel.Factory
) {
    val tabTitles = listOf("Shifts", "Leave Requests", "Announcement", "Performance Reports")
    val tabIcons = listOf(
        Icons.Default.List,
        Icons.Default.MailOutline,
        Icons.Default.Notifications,
        Icons.Default.BarChart
    )
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Shift") }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = colorResource(id = R.color.buttombar),
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
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(10.dp)
        ) {
            when (selectedTab) {
                0 -> {
                    val viewModel: ShiftListViewModel = viewModel(factory = shiftListViewModelFactory)
                    ShiftListScreenAdmin(
                        viewModel = viewModel,
                        onBack = onBack
                    )
                }
                1 -> LeaveRequestApprovalScreen() // Implement this composable
                2 -> AnnouncementManagementScreen() // Implement this composable
                3 -> PerformanceReportScreen() // Implement this composable
            }
        }
    }
}