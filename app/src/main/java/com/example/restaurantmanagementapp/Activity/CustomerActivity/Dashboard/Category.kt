package com.example.restaurantmanagementapp.Activity.CustomerActivity.Dashboard

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.restaurantmanagementapp.Activity.CustomerActivity.Itemlist.ItemListActivity
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.R_mDataBase.entity.CategoryEntity
import com.example.restaurantmanagementapp.ViewModel.CategoryListViewModel
import com.example.restaurantmanagementapp.ui.util.safePainterResource


//Data class for category
data class Category(
    val id: Int,
    val name: String,
    val categoryRes: Int
)
// Reactive and shared mutable list of categories
//val categories : SnapshotStateList<Category> =  mutableStateListOf(
//        Category("Burger", R.drawable.cat1),
//        Category("Chicken", R.drawable.cat2),
//        Category("Pizza", R.drawable.cat3),
//        Category("Sushi", R.drawable.cat4),
//        Category("Pizza", R.drawable.cat5),
//        Category("Pizza", R.drawable.cat6),
//        Category("Pizza", R.drawable.cat7),
//        Category("Pizza", R.drawable.cat8),
//        Category("Pizza", R.drawable.cat9),
//)

@Composable
fun CategoryGrid(
    modifier: Modifier = Modifier,
    viewModel: CategoryListViewModel,
    //categories: List<Category> = com.example.restaurantmanagementapp.Activity.CustomerActivity.Dashboard.categories,
    onCategoryClick: (CategoryEntity) -> Unit = {}
){
    val categories by viewModel.categories.collectAsState()
    val context = LocalContext.current


    Column(
        modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Choose from Menu",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories.size) { index: Int ->
                val category = categories[index]
                CategoryItem(
                    category = category,
                    onClick = {
                        // Navigate to ItemListActivity with category id/name as extra
                        val intent = Intent(context, ItemListActivity::class.java).apply {
                            putExtra("categoryId", category.id)
                            putExtra("categoryName", category.name)
                        }
                        context.startActivity(intent)
                        onCategoryClick(category)
                    }
                )
            }
        }
    }
}

// to display a single category item
@Composable
fun CategoryItem(category: CategoryEntity, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .background(Color(0xFFF8F6F2), shape = RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val painter =
            safePainterResource(
            resId = if (category.imageRes != 0) category.imageRes else null,
            fallbackResId = R.drawable.placeholder // Use your placeholder image
        )
        Image(
            painter = painter,
            contentDescription = category.name,
            modifier = Modifier.size(56.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = category.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF222222),
            textAlign = TextAlign.Center
        )
    }
}

//@Composable
//fun prepareCategory(): MutableList<Category> {
//    val categoryList = remember { mutableStateListOf(
//        Category("Pizza", R.drawable.cat1),
//        Category("Pizza", R.drawable.cat2),
//        Category("Pizza", R.drawable.cat3),
//        Category("Pizza", R.drawable.cat4),
//        Category("Pizza", R.drawable.cat5),
//        Category("Pizza", R.drawable.cat6),
//        Category("Pizza", R.drawable.cat7),
//        Category("Pizza", R.drawable.cat8),
//        Category("Pizza", R.drawable.cat9),
//
//    )}
//    return categoryList
//}