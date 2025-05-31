package com.example.restaurantmanagementapp.Activity.EmployeeActivity

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.example.restaurantmanagementapp.Activity.BaseActivity
import com.example.restaurantmanagementapp.Repository.ShiftRepository
import com.example.restaurantmanagementapp.R_mDataBase.AppDatabase

class EmployeeMainActivity : BaseActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Get username (employeeId) from Intent
        val username = intent.getStringExtra("username")
        if (username.isNullOrEmpty()) {
            // Handle error: No username passed
            Toast.makeText(this, "Username not passed",
                Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // 2. Get repository (using your Room database singleton)
        val db = AppDatabase.getDatabase(applicationContext)
        val shiftRepository = ShiftRepository(db.ShiftDao())

        setContent {
            EmployeeHomeScreen(
                employeeId = username,
                repository = shiftRepository,
                onRequestClick = { date ->
                    // TODO: Handle request click
                }
            )
        }
    }
}