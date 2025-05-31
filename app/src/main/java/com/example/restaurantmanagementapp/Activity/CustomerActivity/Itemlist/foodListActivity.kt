package com.example.restaurantmanagementapp.Activity.CustomerActivity.Itemlist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurantmanagementapp.Activity.BaseActivity
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.R_mDataBase.AppDatabase
import com.example.restaurantmanagementapp.R_mDataBase.entity.FoodEntity
import com.example.restaurantmanagementapp.Repository.FoodItemsRepository
import com.example.restaurantmanagementapp.ViewModel.CategoriesFoodListViewModel
import com.example.restaurantmanagementapp.ViewModel.CategoriesFoodListViewModelFactory
import com.example.restaurantmanagementapp.ui.util.safePainterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class ItemListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Get extras from intent
        val categoryId = intent.getIntExtra("categoryId", -1)
        val categoryName = intent.getStringExtra("categoryName") ?: "Menu"

        // Defensive: If categoryId is invalid, finish activity
        if (categoryId == -1) {
            Toast.makeText(this, "Invalid category", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // ViewModel setup using factory
        val foodDao = AppDatabase.getDatabase(applicationContext).FoodDao()
        val repository = FoodItemsRepository(foodDao)
        val viewModelFactory = CategoriesFoodListViewModelFactory(repository, categoryId)
        val viewModel: CategoriesFoodListViewModel by viewModels { viewModelFactory }

        setContent {
            val foodItems by viewModel.foodItems.collectAsState()
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "foodList") {
                composable("foodList") {
                    CustomerFoodListScreen(
                        title = categoryName,
                        foodItems = foodItems,
                        onBack = { finish() },
                        onFoodClick = { foodItem ->
                            navController.navigate("foodDetail/${foodItem.id}")
                        },
                        viewModel = viewModel
                    )
                }
                composable("foodDetail/{foodId}") { backStackEntry ->
                    val foodId = backStackEntry.arguments?.getString("foodId")?.toIntOrNull() ?: return@composable
                    FoodItemDetailScreen(
                        foodId = foodId,
                        onBack = { navController.popBackStack() },
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerFoodListScreen(
    title: String,
    foodItems: List<FoodEntity>,
    onBack: () -> Unit,
    onFoodClick: (FoodEntity) -> Unit,
    viewModel: CategoriesFoodListViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold, fontSize = 22.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF161515))
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            if (foodItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No food items found.", color = Color.Gray)
                }
            } else {

                foodItems.forEach { food ->
                    FoodListItem(
                        foodItem = food,
                        onClick = { onFoodClick(food) },
                        onFavoriteClick = { isFavorite ->
                            viewModel.updateFavoriteState(food.id, isFavorite)
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun FoodListItem(
    foodItem: FoodEntity,
    onClick: () -> Unit,
    onFavoriteClick: (Boolean) -> Unit // now required
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDAD1D1)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Use safePainterResource for image loading
            val painter = safePainterResource(
                resId = foodItem.ImagePath.toIntOrNull(),
                fallbackResId = R.drawable.placeholder
            )
            Image(
                painter = painter,
                contentDescription = foodItem.name,
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = foodItem.name.ifBlank { "No Name" },
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Price and Calorie Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "£${if (foodItem.Price > 0.0) "%.2f".format(foodItem.Price) else "5.99"}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color(0xFF07180D)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "${if (foodItem.Calorie > 0) foodItem.Calorie else 350} kcal",
                        fontSize = 13.sp,
                        color = Color(0xFFA54444)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                // Star and Title Row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.star), // Replace with your star icon
                        contentDescription = "Star",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = foodItem.Star.ifBlank { "4.5" },
                        fontSize = 13.sp,
                        color = Color(0xFF07180D)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = foodItem.Title.ifBlank { "Popular" },
                        fontSize = 13.sp,
                        color = Color(0xFF07180D)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.time),
                        contentDescription = "Time",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${if (foodItem.TimeValue > 0) foodItem.TimeValue else 15} min",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                // Time Row
            }
            Spacer(modifier = Modifier.height(16.dp))
            Icon(
                painter = painterResource(id = R.drawable.fav_icon),
                contentDescription = "LikeIcon",
                tint = if (foodItem.Bestfood) Color.Red else Color.Gray,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        onFavoriteClick(!foodItem.Bestfood)
                    }
                    .clip(RoundedCornerShape(12.dp))
            )
        }
    }
}