package com.example.restaurantmanagementapp.Repository

import com.example.restaurantmanagementapp.R_mDataBase.dao.FoodDao
import com.example.restaurantmanagementapp.R_mDataBase.entity.FoodEntity

class FoodItemsRepository(private val foodDao: FoodDao) {
    suspend fun getFoodItemsByCategory(categoryId: Int): List<FoodEntity> =
        foodDao.getFoodItemsByCategory(categoryId)

    suspend fun searchFoodItems(categoryId: Int, query: String): List<FoodEntity> =
        foodDao.searchFoodItems(categoryId, query)

    suspend fun insertFoodItem(food: FoodEntity) {
        foodDao.insert(food)
    }

    suspend fun updateFoodItem(food: FoodEntity) {
        foodDao.update(food)
    }

    suspend fun deleteFoodItems(foods: List<FoodEntity>) {
        foodDao.deleteAll(foods)
    }
    suspend fun updateFavoriteState(foodId: Int, isFavorite: Boolean) {
        foodDao.updateFavoriteState(foodId, isFavorite)
    }

    suspend fun getFavoriteFoods(): List<FoodEntity> {
        return foodDao.getFavoriteFoods()
    }

}