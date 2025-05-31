package com.example.restaurantmanagementapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.restaurantmanagementapp.R_mDataBase.entity.UserEntity
import com.example.restaurantmanagementapp.Repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserListViewModel(
    private val repository: UserRepository,
    private val role: String
) : ViewModel() {

    private val _users = MutableStateFlow<List<UserEntity>>(emptyList())
    val users: StateFlow<List<UserEntity>> = _users

    private val _selectedUsers = MutableStateFlow<Set<Int>>(emptySet())
    val selectedUsers: StateFlow<Set<Int>> = _selectedUsers

    private val _showAddEditDialog = MutableStateFlow(false)
    val showAddEditDialog: StateFlow<Boolean> = _showAddEditDialog

    private val _editingUser = MutableStateFlow<UserEntity?>(null)
    val editingUser: StateFlow<UserEntity?> = _editingUser

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _users.value = repository.getUsersByRole(role)
        }
    }

    fun onUserClick(userId: Int) {
        _selectedUsers.value = if (_selectedUsers.value.contains(userId)) {
            _selectedUsers.value - userId
        } else {
            _selectedUsers.value + userId
        }
    }

    fun onAddUser() {
        _editingUser.value = null
        _showAddEditDialog.value = true
    }

    fun onEditSelected() {
        val selected = _selectedUsers.value.firstOrNull()
        val user = _users.value.find { it.id == selected }
        _editingUser.value = user
        _showAddEditDialog.value = true
    }

    fun onDeleteSelected() {
        viewModelScope.launch {
            val usersToDelete = _users.value.filter { _selectedUsers.value.contains(it.id) }
            repository.deleteUsers(usersToDelete)
            _selectedUsers.value = emptySet()
            loadUsers()
        }
    }

    fun addOrUpdateUser(username: String, password: String, id: Int? = null) {
        viewModelScope.launch {
            if (id == null) {
                repository.insertUser(UserEntity(username = username, password = password, role = role))
            } else {
                repository.updateUser(UserEntity(id = id, username = username, password = password, role = role))
            }
            _showAddEditDialog.value = false
            loadUsers()
        }
    }

    fun dismissDialog() {
        _showAddEditDialog.value = false
    }

    class Factory(
        private val repository: UserRepository,
        private val role: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return UserListViewModel(repository, role) as T
        }
    }
}