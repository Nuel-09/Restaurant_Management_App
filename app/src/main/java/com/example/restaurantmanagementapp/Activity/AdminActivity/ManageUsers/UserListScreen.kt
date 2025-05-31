package com.example.restaurantmanagementapp.Activity.AdminActivity.ManageUser

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.restaurantmanagementapp.Activity.AdminActivity.ManageUsers.UserAddEditDialog
import com.example.restaurantmanagementapp.R_mDataBase.entity.UserEntity
import com.example.restaurantmanagementapp.ViewModel.UserListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    viewModel: UserListViewModel,
    role: String,
    onBack: () -> Unit
) {
    val users by viewModel.users.collectAsState()
    val selectedUsers by viewModel.selectedUsers.collectAsState()
    val showDialog by viewModel.showAddEditDialog.collectAsState()
    val editingUser by viewModel.editingUser.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage ${role.replaceFirstChar { it.uppercase() }} ") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (role != "customer") {
                        IconButton(onClick = { viewModel.onAddUser() }) {
                            Icon(Icons.Default.Add, contentDescription = "Add User")
                        }
                    }
                    IconButton(
                        onClick = { viewModel.onEditSelected() },
                        enabled = selectedUsers.size == 1
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit User")
                    }
                    IconButton(
                        onClick = { viewModel.onDeleteSelected() },
                        enabled = selectedUsers.isNotEmpty()
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete User")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(users.size) { index ->
                val user = users[index]
                UserListItem(
                    user = user,
                    isSelected = selectedUsers.contains(user.id),
                    onClick = { viewModel.onUserClick(user.id) }
                )
            }
        }
    }

    UserAddEditDialog(
        show = showDialog,
        initialUsername = editingUser?.username ?: "",
        initialPassword = editingUser?.password ?: "",
        isEdit = editingUser != null,
        onDismiss = { viewModel.dismissDialog() },
        onConfirm = { username, password ->
            viewModel.addOrUpdateUser(
                username = username,
                password = password,
                id = editingUser?.id
            )
        },
        canEditPassword = true,
        canEditUsername = true,
        showAdd = role != "customer"
    )
}

@Composable
fun UserListItem(
    user: UserEntity,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(user.username, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))
            Text(user.role, style = MaterialTheme.typography.bodySmall)
        }
    }
}