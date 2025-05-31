package com.example.restaurantmanagementapp.R_mDataBase.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String="",
    val imageRes: Int = 0
)