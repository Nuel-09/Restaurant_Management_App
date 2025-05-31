package com.example.restaurantmanagementapp.Activity.CustomerActivity.Itemlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.ViewModel.CategoriesFoodListViewModel

// ViewModel-integrated version
@Composable
fun FoodItemDetailScreen(
    foodId: Int,
    onBack: () -> Unit,
    viewModel: CategoriesFoodListViewModel = viewModel()
) {
    val foodItems by viewModel.foodItems.collectAsStateWithLifecycle()
    val foodItem = foodItems.find { it.id == foodId }
    var quantity by remember { mutableStateOf(1) }
    var showAddedSnackbar by remember { mutableStateOf(false) }

    // Colors
    val buttonColor = Color(0xFFF7E03F3A)
    val priceColor = Color(0xFF23262F)

    if (foodItem == null) {
        // Show loading or error state
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...", color = Color.Gray)
        }
        return
    }

    var favState by remember(foodItem.Bestfood) { mutableStateOf(foodItem.Bestfood) }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF7F7F7))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Food Image with overlay icons
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
            ) {
                // Use image resource safely (if possible)
                val imageRes = foodItem.ImagePath.toIntOrNull() ?: R.drawable.food1
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = foodItem.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Top icons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Back Icon
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color.White.copy(alpha = 0.7f), CircleShape)
                            .clip(CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.back),
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    // Favorite Icon
                    IconButton(
                        onClick = {
                            favState = !favState
                            viewModel.updateFavoriteState(foodItem.id, favState)
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color.White.copy(alpha = 0.7f), CircleShape)
                            .clip(CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.fav_icon),
                            contentDescription = "Favorite",
                            tint = if (favState) Color.Red else Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
            // Arch Card (overlapping)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-60).dp)
                    .zIndex(1f)
                    .padding(bottom = 24.dp, start = 24.dp, end = 24.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp,
                        bottomStart = 32.dp, bottomEnd = 32.dp ),
                    shadowElevation = 8.dp,
                    color = colorResource(id = R.color.arch_card_background),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = foodItem.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = priceColor
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Prep time
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.time_color),
                                    contentDescription = "Time",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${foodItem.TimeValue} min",
                                    fontSize = 15.sp,
                                    color = Color.Gray
                                )
                            }
                            // Rating
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.star),
                                    contentDescription = "Rating",
                                    tint = Color(0xFFF7D95854),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = foodItem.Star.ifBlank { "4.5" },
                                    fontSize = 15.sp,
                                    color = Color.Gray
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.flame),
                                    contentDescription = "Calories",
                                    tint = Color.Red,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${foodItem.Calorie}",
                                    fontSize = 15.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(18.dp))
                        // Price and Quantity
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "$${"%.2f".format(foodItem.Price * quantity)}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = priceColor
                            )
                            Spacer(modifier = Modifier.width(24.dp))
                            // Quantity controls
                            OutlinedButton(
                                onClick = { if (quantity > 1) quantity-- },
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Text("-", fontSize = 20.sp)
                            }
                            Text(
                                text = quantity.toString(),
                                modifier = Modifier.padding(horizontal = 12.dp),
                                fontSize = 18.sp
                            )
                            OutlinedButton(
                                onClick = { quantity++ },
                                shape = CircleShape,
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Text("+", fontSize = 20.sp)
                            }
                        }
                    }
                }
            }

            // Description outside the arch card
            foodItem.Description.takeIf { it.isNotBlank() }?.let {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .offset(y = (-40).dp)
                ) {
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
        // Add to Basket Button at the bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(colorResource(id = R.color.Add_to_basket))
                .padding(bottom = 24.dp, start = 24.dp, end = 24.dp)
                .background(colorResource(id = R.color.Add_to_basket)),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 24.dp, start = 24.dp, end = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total: $${"%.2f".format(foodItem.Price * quantity)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = priceColor
                )
                Button(
                    onClick = {
                        // You can add to basket logic here
                        showAddedSnackbar = true
                    },
                    modifier = Modifier.height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text("Add to Basket", fontSize = 18.sp, color = Color.White)
                }
            }
        }

        // Snackbar for confirmation
        if (showAddedSnackbar) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 90.dp),
                containerColor = priceColor,
                action = {
                    TextButton(onClick = { showAddedSnackbar = false }) {
                        Text("Dismiss", color = Color.White)
                    }
                }
            ) {
                Text("Added to basket!", color = Color.White)
            }
            // Auto-dismiss after 2 seconds
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(2000)
                showAddedSnackbar = false
            }
        }
    }
}

// No preview for DB-backed screen