package com.example.restaurantmanagementapp.Activity.AdminActivity.ManageUsers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun UserAddEditDialog(
    show: Boolean,
    initialUsername: String,
    initialPassword: String,
    isEdit: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (username: String, password: String) -> Unit,
    canEditUsername: Boolean,
    canEditPassword: Boolean,
    showAdd: Boolean
) {
    if (!show) return

    var username by remember { mutableStateOf(initialUsername) }
    var password by remember { mutableStateOf(initialPassword) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = { onConfirm(username, password) },
                enabled = username.isNotBlank() && password.isNotBlank() && showAdd
            ) {
                Text(if (isEdit) "Update" else "Add")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text(if (isEdit) "Edit User" else "Add User") },
        text = {
            Column {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    enabled = canEditUsername,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    enabled = canEditPassword,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )
            }
        }
    )
}