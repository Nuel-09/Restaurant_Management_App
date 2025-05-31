package com.example.restaurantmanagementapp.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurantmanagementapp.R_mDataBase.entity.CategoryEntity
import com.example.restaurantmanagementapp.Repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryListViewModel(
   private val repository: CategoryRepository
) : ViewModel() { // coroutine feature of requesting data in a asynchronous way

    private val _categories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val categories: StateFlow<List<CategoryEntity>> = _categories

    private val _selectedCategories = MutableStateFlow<Set<Int>>(emptySet())
    val selectedCategories: StateFlow<Set<Int>> = _selectedCategories

    // for dialog state
    private val _showAddEditDialog = MutableStateFlow(false)
    val showAddEditDialog: StateFlow<Boolean> = _showAddEditDialog

    private val _editingCategory = MutableStateFlow<CategoryEntity?> (null)
    val editingCategory: StateFlow<CategoryEntity?> = _editingCategory

    init { // calls the func
        loadCategories()
    }
    // loading all  categories
    fun loadCategories() {
        viewModelScope.launch {
            _categories.value = repository.getAllCategories()
        }
    }
    // search categories based on query or list all categories is query is null
    fun searchCategories(query: String) {
        viewModelScope.launch {
            _categories.value = if (query.isBlank()) {
                repository.getAllCategories()
            } else {
                repository.searchCategories(query)
            }
        }
    }

    fun onCategoryClick(categoryId: Int) {
        _selectedCategories.value = if (_selectedCategories.value.contains(categoryId)) {
            _selectedCategories.value -categoryId}
        else{
            _selectedCategories.value + categoryId
        }
    }

    fun onAddCategory() { // display the dialog box with null values
        _editingCategory.value = null
        _showAddEditDialog.value = true
    }

    fun onEditSelected() {
        val selected = _selectedCategories.value.firstOrNull()
        val category = _categories.value.find { it.id == selected }
        _editingCategory.value = category
        _showAddEditDialog.value = true
    }

    fun onDeleteSelected() {
        viewModelScope.launch {
            val categoriesToDelete = _categories.value.filter { _selectedCategories.value.contains(it.id) }
            repository.deleteCategories(categoriesToDelete)
            _selectedCategories.value = emptySet()
            loadCategories()
        }
    }

    fun addOrUpdateCategory(name: String, imageRes: Int, id: Int? = null) {
        viewModelScope.launch {
            if (id == null) {
                // Add new category
                repository.insertCategory(CategoryEntity(name = name, imageRes = imageRes))
            } else {
                // Update existing category
                repository.updateCategory(CategoryEntity(id = id, name = name, imageRes = imageRes))
            }
            _showAddEditDialog.value = false
            loadCategories()
        }
    }

    fun dismissDialog() {
        _showAddEditDialog.value = false
    }
}