package com.example.restaurantmanagementapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantmanagementapp.Repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class SignupResult {
    object Loading : SignupResult()
    data class Success(val username: String,  val userId: Int, val role: String) : SignupResult()
    data class Error(val message: String) : SignupResult()
    object Idle : SignupResult()
}

class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _signupResult = MutableStateFlow<SignupResult>(SignupResult.Idle)
    val signupResult: StateFlow<SignupResult> = _signupResult

    fun signup(username: String, password: String, role: String) {
        // Input validation
        if (username.isBlank() || password.isBlank()) {
            _signupResult.value = SignupResult.Error("Username and password cannot be empty")
            return
        }
        _signupResult.value = SignupResult.Loading
        viewModelScope.launch {
            try {
                val userId = userRepository.registerUser(username, password, role)
                if (userId != null && userId > 0) {
                    _signupResult.value = SignupResult.Success(username, userId, role)
                } else {
                    _signupResult.value = SignupResult.Error("Signup failed, check your credentials and try again.")
                }
            } catch (e: Exception) {
                _signupResult.value = SignupResult.Error("Signup error: ${e.message}")
            }
        }
    }
}