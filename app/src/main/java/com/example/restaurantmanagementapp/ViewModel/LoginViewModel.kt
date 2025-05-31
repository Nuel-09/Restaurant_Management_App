package com.example.restaurantmanagementapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantmanagementapp.Repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class LoginResult { // defines status and state for the login operations
    object Loading : LoginResult()
    data class Success(val username: String, val role: String) : LoginResult()
    data class Error(val message: String) : LoginResult()
    object Idle : LoginResult()
}

class LoginViewModel(private val userRepository: UserRepository
) : ViewModel() {

    private val _loginResult =  MutableStateFlow<LoginResult>(LoginResult.Idle)
    val loginResult: StateFlow<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        _loginResult.value = LoginResult.Loading
        viewModelScope.launch {
            val user = userRepository.authenticate(username, password)
            if (user != null) {
                _loginResult.value =
                    LoginResult.Success(user.username, user.role) // include user's role to help Activity Nav
            } else {
                _loginResult.value = LoginResult.Error("Invalid username or password")
            }
        }
    }
}
