package com.example.restaurantmanagementapp.R_mDataBase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.restaurantmanagementapp.R_mDataBase.entity.CategoryEntity
import com.example.restaurantmanagementapp.R_mDataBase.entity.FoodEntity

@Dao
interface FoodDao: BaseDao<FoodEntity> {
    @Query("SELECT * FROM food WHERE categoryId = :categoryId")
    suspend fun getFoodItemsByCategory(categoryId:Int): List<FoodEntity>

    @Query("SELECT * FROM food WHERE categoryId = :categoryId AND name LIKE '%' || :query || '%'")
    suspend fun searchFoodItems(categoryId: Int, query: String): List<FoodEntity>

    @Delete
    suspend fun deleteAll(foods: List<FoodEntity>)

    @Query("UPDATE Food SET Bestfood = :isFavorite WHERE id = :foodId")
    suspend fun updateFavoriteState(foodId: Int, isFavorite: Boolean)

    @Query("SELECT * FROM Food WHERE Bestfood = 1")
    suspend fun getFavoriteFoods(): List<FoodEntity>

}