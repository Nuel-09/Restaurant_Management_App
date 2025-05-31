
package com.example.restaurantmanagementapp.Activity.AdminActivity.ManageMenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.restaurantmanagementapp.ui.util.safePainterResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.restaurantmanagementapp.Activity.AdminActivity.availableFoodImages
import com.example.restaurantmanagementapp.R_mDataBase.entity.FoodEntity
import com.example.restaurantmanagementapp.ViewModel.CategoriesFoodListViewModel
import com.example.restaurantmanagementapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFoodListScreen(
    viewModel: CategoriesFoodListViewModel,
    categoryName: String,
    categoryId: Int,
    onBack: () -> Unit
) {
    val foodItems by viewModel.foodItems.collectAsState()
    val selectedFoodItems by viewModel.selectedFoodItems.collectAsState()
    val showDialog by viewModel.showAddEditDialog.collectAsState()
    val editingFood by viewModel.editingFood.collectAsState()

    var showSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Food List (${categoryName})") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { showSearch = !showSearch }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                        IconButton(onClick = { viewModel.onAddFoodItem() }) {
                            Icon(Icons.Default.Add, contentDescription = "Add Food")
                        }
                        IconButton(
                            onClick = { viewModel.onEditSelected() },
                            enabled = selectedFoodItems.size == 1
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Food")
                        }
                        IconButton(
                            onClick = { viewModel.onDeleteSelected() },
                            enabled = selectedFoodItems.isNotEmpty()
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Food")
                        }
                    }
                )
                if (showSearch) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            viewModel.searchFoodItems(it)
                        },
                        label = { Text("Search Food Items") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        singleLine = true,
                        trailingIcon = {
                            IconButton(onClick = {
                                showSearch = false
                                searchQuery = ""
                                viewModel.searchFoodItems("")
                            }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Close Search")
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(120.dp),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(foodItems.size) { index ->
                val food = foodItems[index]
                FoodGridItem(
                    food = food,
                    isSelected = selectedFoodItems.contains(food.id),
                    onClick = { viewModel.onFoodItemClick(food.id) }
                )
            }
        }
    }

    FoodAddEditDialog(
        show = showDialog,
        initialName = editingFood?.name ?: "",
        initialDescription = editingFood?.Description ?: "",
        initialImagePath = editingFood?.ImagePath ?: "",
        initialPrice = editingFood?.Price?.toString() ?: "",
        initialTimeValue = editingFood?.TimeValue?.toString() ?: "",
        initialCalorie = editingFood?.Calorie?.toString() ?: "",
        availableImages = availableFoodImages,
        onDismiss = { viewModel.dismissDialog() },
        onConfirm = { name, description, imagePath, price, timeValue, calorie, id ->
            viewModel.addOrUpdateFood(
                name = name,
                description = description,
                imagePath = imagePath,
                price = price,
                timeValue = timeValue,
                calorie = calorie,
                id = id
            )
        }
    )
}




@Composable
fun FoodGridItem(
    food: FoodEntity,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val painter = safePainterResource(
        resId = food.ImagePath.toIntOrNull(),
        fallbackResId = R.drawable.placeholder // Ensure this drawable exists
    )

    Card(
        modifier = Modifier
            .padding(8.dp)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                else Color.Transparent
            )
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painter,
                contentDescription = food.name,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(food.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("£${food.Price}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.time),
                    contentDescription = "Time Icon",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${food.TimeValue} min",
                    color= Color(0xFF0CF058),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
