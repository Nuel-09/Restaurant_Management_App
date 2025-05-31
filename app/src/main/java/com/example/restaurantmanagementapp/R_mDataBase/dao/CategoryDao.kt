package com.example.restaurantmanagementapp.R_mDataBase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.example.restaurantmanagementapp.R_mDataBase.entity.CategoryEntity


@Dao // inherits the parent functionalities in baseDase
interface CategoryDao : BaseDao<CategoryEntity>{
    @Query("SElECT * FROM categories ORDER BY name ASC")
    suspend fun getAllCategories():List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE id = :id LIMIT 1")
    suspend fun getCategoryByID(id: Int): CategoryEntity

    @Delete
    suspend fun deleteCategories(categories: List<CategoryEntity>)

    @Query("SELECT * FROM categories WHERE name LIKE :query")
    suspend fun searchCategories(query: String): List<CategoryEntity>
}