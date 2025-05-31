package com.example.restaurantmanagementapp.Activity.AdminActivity.ManageAdminNav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.restaurantmanagementapp.R


@Composable
fun DashboardScreen(
    onNavigateToCategories: () -> Unit,
    onNavigateToUsers: () -> Unit,
    onNavigateToShifts: () -> Unit,
    onNavigateToPayroll: () -> Unit
) {
    val manageCategoriesColor = colorResource(id = R.color.manageCategories)
    val manageUsersColor = colorResource(id = R.color.manageusers)
    val manageShiftsColor = colorResource(id = R.color.manageshift)
    val managePayrollColor = colorResource(id = R.color.managepayroll)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onNavigateToCategories() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = manageCategoriesColor)
        ) {
            Text("Manage Categories and Food List")
        }
        Button(
            onClick = { onNavigateToUsers() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = manageUsersColor)
        ) {
            Text("Manage Users")
        }
        Button(
            onClick = { onNavigateToShifts() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = manageShiftsColor)
        ) {
            Text("Manage Shifts")
        }
        Button(
            onClick = { onNavigateToPayroll() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = managePayrollColor)
        ) {
            Text("Manage Payroll and Invoice")
        }
    }
}
