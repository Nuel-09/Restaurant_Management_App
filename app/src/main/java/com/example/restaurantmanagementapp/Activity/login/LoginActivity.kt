package com.example.restaurantmanagementapp.Activity.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantmanagementapp.Activity.AdminActivity.ManageAdminNav.AdminMainActivity
import com.example.restaurantmanagementapp.Activity.BaseActivity
import com.example.restaurantmanagementapp.Activity.EmployeeActivity.EmployeeMainActivity
import com.example.restaurantmanagementapp.Activity.signUp.SignupActivity
import com.example.restaurantmanagementapp.R_mDataBase.AppDatabase
import com.example.restaurantmanagementapp.Repository.UserRepository
import com.example.restaurantmanagementapp.ViewModel.LoginViewModel


class LoginActivity : BaseActivity() {
    //Instantiating LoginViewModel and create custom viewmodel custom factory to provide UserRepository
    private val viewModel: LoginViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val db = AppDatabase.getDatabase(applicationContext)
                val userRepository = UserRepository(db.UserDao())
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(userRepository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = { username, role ->
                    when (role) {
                        "admin" -> {
                            startActivity(Intent(this, AdminMainActivity::class.java))
                        }

                        "employee" -> {
                            val intent = Intent(this, EmployeeMainActivity::class.java)
                            intent.putExtra("username", username) // username is a String
                            startActivity(intent)
                        }

                        "customer" -> {
                        }

                        else -> {
                            // Optionally handle unknown roles
                        }
                    }
                    finish()
                },
                onSignupClick = {
                    startActivity(Intent(this, SignupActivity::class.java))
                }
            )
        }
    }
}