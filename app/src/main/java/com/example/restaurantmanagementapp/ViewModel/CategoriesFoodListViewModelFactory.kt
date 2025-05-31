package com.example.restaurantmanagementapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantmanagementapp.Repository.FoodItemsRepository

class CategoriesFoodListViewModelFactory(
    private val repository: FoodItemsRepository,
    private val categoryId: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesFoodListViewModel::class.java)) {
            return CategoriesFoodListViewModel(repository, categoryId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}