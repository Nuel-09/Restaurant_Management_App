package com.example.restaurantmanagementapp.R_mDataBase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import okio.Path


@Entity(
    tableName = "Food",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class FoodEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "categoryId")
    val categoryId: Int = 0,
    val Description: String="",
    val ImagePath: String="",
    val LocationID: String="",
    val Price: Double=0.0,
    val PriceId: Int=0,
    val Star: String="",
    val Title: String="",
    val name: String="",
    val Calorie:Int=0,
    val numberInCar:Int=0,
    val TimeId: Int=0,
    val Bestfood: Boolean=false,
    val TimeValue: Int=0

)
