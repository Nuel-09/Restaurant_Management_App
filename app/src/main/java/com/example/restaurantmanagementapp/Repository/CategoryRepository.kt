package com.example.restaurantmanagementapp.Repository

import com.example.restaurantmanagementapp.R_mDataBase.dao.CategoryDao
import com.example.restaurantmanagementapp.R_mDataBase.entity.CategoryEntity

class CategoryRepository(private val categoryDao: CategoryDao) {

    suspend fun getAllCategories(): List<CategoryEntity> =
        categoryDao.getAllCategories()

    suspend fun getCategoryById(id: Int): CategoryEntity =
        categoryDao.getCategoryByID(id)

    suspend fun searchCategories(query: String): List<CategoryEntity> =
        categoryDao.searchCategories("%$query%")

    suspend fun insertCategory(category: CategoryEntity) =
        categoryDao.insert(category)

    suspend fun updateCategory(category: CategoryEntity) =
        categoryDao.update(category)

    suspend fun deleteCategory(category: CategoryEntity) =
        categoryDao.delete(category)

    suspend fun deleteCategories(categories: List<CategoryEntity>) =
        categoryDao.deleteCategories(categories)
}