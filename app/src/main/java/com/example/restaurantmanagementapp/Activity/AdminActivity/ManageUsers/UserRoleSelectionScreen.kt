package com.example.restaurantmanagementapp.Activity.AdminActivity.ManageUser

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.restaurantmanagementapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRoleSelectionScreen(
    onRoleSelected: (String) -> Unit,
) {
    val AdminColor = colorResource(id = R.color.manageusers)
    val EmployeeColor = colorResource(id = R.color.manageshift)
    val CustomerColor = colorResource(id = R.color.managepayroll)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Users") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { onRoleSelected("admin") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AdminColor)
            ) { Text("Admin") }
            Button(
                onClick = { onRoleSelected("employee") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmployeeColor)
            ) { Text("Employee") }
            Button(
                onClick = { onRoleSelected("customer") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CustomerColor)
            ) { Text("Customer") }
        }
    }
}