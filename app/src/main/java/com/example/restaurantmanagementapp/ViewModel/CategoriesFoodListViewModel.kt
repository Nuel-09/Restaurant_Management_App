package com.example.restaurantmanagementapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantmanagementapp.R_mDataBase.entity.FoodEntity
import com.example.restaurantmanagementapp.Repository.FoodItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoriesFoodListViewModel(
    private val repository: FoodItemsRepository,
    private val categoryId: Int
) : ViewModel() {

    private val _foodItems = MutableStateFlow<List<FoodEntity>>(emptyList())
    val foodItems: StateFlow<List<FoodEntity>> = _foodItems

    private val _selectedFoodItems = MutableStateFlow<Set<Int>>(emptySet())
    val selectedFoodItems: StateFlow<Set<Int>> = _selectedFoodItems

    // For dialog state management
    private val _showAddEditDialog = MutableStateFlow(false)
    val showAddEditDialog: StateFlow<Boolean> = _showAddEditDialog

    private val _editingFood: MutableStateFlow<FoodEntity?> = MutableStateFlow(null)
    val editingFood: StateFlow<FoodEntity?> = _editingFood

    init {
        loadFoodItems()
    }

    fun loadFoodItems() {
        viewModelScope.launch {
            _foodItems.value = repository.getFoodItemsByCategory(categoryId)
        }
    }

    fun searchFoodItems(query: String) {
        viewModelScope.launch {
            _foodItems.value = if (query.isBlank()) {
                repository.getFoodItemsByCategory(categoryId)
            } else {
                repository.searchFoodItems(categoryId, query)
            }
        }
    }

    fun onFoodItemClick(foodId: Int) {
        _selectedFoodItems.value = if (_selectedFoodItems.value.contains(foodId)) {
            _selectedFoodItems.value - foodId
        } else {
            _selectedFoodItems.value + foodId
        }
    }

    fun onAddFoodItem() {
        // Show dialog for adding new food (null means new)
        _editingFood.value = null
        _showAddEditDialog.value = true
    }

    fun onEditSelected() {
        val selected = _selectedFoodItems.value.firstOrNull()
        val food = _foodItems.value.find { it.id == selected }
        _editingFood.value = food
        _showAddEditDialog.value = true
    }

    fun onDeleteSelected() {
        viewModelScope.launch {
            val foodsToDelete = _foodItems.value.filter { _selectedFoodItems.value.contains(it.id) }
            repository.deleteFoodItems(foodsToDelete)
            _selectedFoodItems.value = emptySet()
            loadFoodItems()
        }
    }

    fun addOrUpdateFood(
        name: String,
        description: String,
        imagePath: String,
        price: Double,
        calorie: Int = 0,
        title: String = "",
        bestFood: Boolean = false,
        timeId: Int = 0,
        timeValue: Int = 0,
        locationId: String = "",
        priceId: Int = 0,
        numberInCar: Int = 0,
        id: Int? = null
    ) {
        viewModelScope.launch {
            if (id == null) {
                // Add new food item
                repository.insertFoodItem(
                    FoodEntity(
                        name = name,
                        Description = description,
                        ImagePath = imagePath,
                        Price = price,
                        Calorie = calorie,
                        Title = title,
                        Bestfood = bestFood,
                        TimeId = timeId,
                        TimeValue = timeValue,
                        LocationID = locationId,
                        PriceId = priceId,
                        numberInCar = numberInCar,
                        categoryId = categoryId // always set to current category
                    )
                )
            } else {
                // Update existing food item
                repository.updateFoodItem(
                    FoodEntity(
                        id = id,
                        name = name,
                        Description = description,
                        ImagePath = imagePath,
                        Price = price,
                        Calorie = calorie,
                        Title = title,
                        Bestfood = bestFood,
                        TimeId = timeId,
                        TimeValue = timeValue,
                        LocationID = locationId,
                        PriceId = priceId,
                        numberInCar = numberInCar,
                        categoryId = categoryId
                    )
                )
            }
            _showAddEditDialog.value = false
            loadFoodItems()
        }
    }
    // update bestfood
    fun updateFavoriteState(foodId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.updateFavoriteState(foodId, isFavorite)
            loadFoodItems() // Refresh list
        }
    }

    //query bestfood for customers
    private val _favoriteFoods = MutableStateFlow<List<FoodEntity>>(emptyList())
    val favoriteFoods: StateFlow<List<FoodEntity>> = _favoriteFoods

    fun loadFavoriteFoods() {
        viewModelScope.launch {
            _favoriteFoods.value = repository.getFavoriteFoods()
        }
    }

    fun dismissDialog() {
        _showAddEditDialog.value = false
    }

}