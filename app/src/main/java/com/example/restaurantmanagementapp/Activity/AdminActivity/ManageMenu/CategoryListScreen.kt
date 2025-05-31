
package com.example.restaurantmanagementapp.Activity.AdminActivity.ManageMenu

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.R_mDataBase.entity.CategoryEntity
import com.example.restaurantmanagementapp.ViewModel.CategoryListViewModel
import com.example.restaurantmanagementapp.ui.util.safePainterResource

// Defined images for categories
val availableImages = listOf(
    R.drawable.cat1,
    R.drawable.cat2,
    R.drawable.cat3,
    R.drawable.cat4,
    R.drawable.cat5,
    R.drawable.cat6,
    R.drawable.cat7,
    R.drawable.cat8,
    R.drawable.cat9
)

@Composable
fun CategoryListScreenWithSafeNavigation(
    viewModel: CategoryListViewModel,
    onCategorySelected: (Int, String) -> Unit,
    onBack: () -> Unit,
    onNavigateToDashboard: () -> Unit
) {
    var hasError by remember { mutableStateOf(false) }

    if (hasError) {
        // Navigate to dashboard or show error UI
        LaunchedEffect(Unit) {
            onNavigateToDashboard()
        }
    } else {
        CategoryListScreen(
            viewModel = viewModel,
            onCategorySelected = onCategorySelected,
            onBack = onBack,
            onError = { hasError = true }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(
    viewModel: CategoryListViewModel,
    onCategorySelected: (Int, String) -> Unit,
    onBack: () -> Unit,
    onError: () -> Unit
) {
    val categories by viewModel.categories.collectAsState(initial = emptyList())
    val selectedCategories by viewModel.selectedCategories.collectAsState(initial = emptySet())
    var showSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val showAddEditDialog by viewModel.showAddEditDialog.collectAsState()
    val editingCategory by viewModel.editingCategory.collectAsState()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Category Section") },
                    navigationIcon = {
                        IconButton(onClick = onBack){
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { showSearch = !showSearch }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                        IconButton(onClick = { /* TODO: Open nav panel */ }) {
                            Icon(Icons.Default.Add, contentDescription = "Nav Panel")
                        }
                    }
                )
                if (showSearch) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it; viewModel.searchCategories(it) },
                        label = { Text("Search Food Catalogues") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        singleLine = true,
                        trailingIcon = {
                            IconButton(onClick = {
                                showSearch = false
                                searchQuery = ""
                                viewModel.searchCategories("")
                            }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Close Search")
                            }
                        }
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onAddCategory() }) {
                Icon(Icons.Default.Add, contentDescription = "Add Category")
            }
        },
        bottomBar = {
            if (selectedCategories.isNotEmpty()) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { viewModel.onEditSelected() }) { Text("Edit") }
                    Button(onClick = { viewModel.onDeleteSelected() }) { Text("Delete") }
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
            items(categories.size) { index ->
                val category = categories[index]
                CategoryGridItem(
                    category = category,
                    isSelected = selectedCategories.contains(category.id),
                    onClick = { viewModel.onCategoryClick(category.id) },
                    onDoubleClick = { onCategorySelected(category.id, category.name) }
                )
            }
        }
        if (showAddEditDialog) {
            AddEditCategoryDialog(
                initialName = editingCategory?.name ?: "",
                initialImageRes = editingCategory?.imageRes,
                onConfirm = { name, imageRes ->
                    viewModel.addOrUpdateCategory(name, imageRes, editingCategory?.id)
                },
                onDismiss = { viewModel.dismissDialog() },
                availableImages = availableImages
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryGridItem(
    category: CategoryEntity,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDoubleClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onDoubleClick = onDoubleClick
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                        else Color.Transparent
                    ),
                contentAlignment = Alignment.Center
            ) {
                val painter = safePainterResource(
                    resId = if (category.imageRes != 0) category.imageRes else null,
                    fallbackResId = R.drawable.placeholder // Using placeholder im as fallback from available images
                )
                Image(
                    painter = painter,
                    contentDescription = category.name,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}
