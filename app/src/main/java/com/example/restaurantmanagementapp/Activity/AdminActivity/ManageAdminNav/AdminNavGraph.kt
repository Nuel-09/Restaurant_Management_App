package com.example.restaurantmanagementapp.Activity.AdminActivity.ManageAdminNav

import CategoryListViewModelFactory
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.restaurantmanagementapp.Activity.AdminActivity.ManageMenu.CategoryFoodListScreen
import com.example.restaurantmanagementapp.Activity.AdminActivity.ManageMenu.CategoryListScreen
import com.example.restaurantmanagementapp.Activity.AdminActivity.ManageMenu.CategoryListScreenWithSafeNavigation
import com.example.restaurantmanagementapp.Activity.AdminActivity.manageShift.ShiftManagementHomeScreen
import com.example.restaurantmanagementapp.R_mDataBase.AppDatabase
import com.example.restaurantmanagementapp.Repository.CategoryRepository
import com.example.restaurantmanagementapp.Repository.FoodItemsRepository
import com.example.restaurantmanagementapp.Repository.ShiftRepository
import com.example.restaurantmanagementapp.Repository.UserRepository
import com.example.restaurantmanagementapp.ViewModel.CategoryListViewModel
import com.example.restaurantmanagementapp.ViewModel.CategoriesFoodListViewModel
import com.example.restaurantmanagementapp.ViewModel.CategoriesFoodListViewModelFactory
import com.example.restaurantmanagementapp.ViewModel.ShiftListViewModel
import com.example.restaurantmanagementapp.ViewModel.UserListViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdminNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "dashboard") {
        composable("dashboard") {
            DashboardScreen(
                onNavigateToCategories = { navController.navigate("categories") },
                onNavigateToUsers = { navController.navigate("users") },
                onNavigateToShifts = { navController.navigate("shifts") },
                onNavigateToPayroll = { navController.navigate("payroll") }
            )
        }
        composable("categories") {
            val context = LocalContext.current
            val db = AppDatabase.getDatabase(context)
            val categoryDao = db.categoryDao()
            val repository = CategoryRepository(categoryDao)
            val factory = CategoryListViewModelFactory(repository)
            val categoryListViewModel: CategoryListViewModel = viewModel(factory = factory)
            CategoryListScreenWithSafeNavigation(
                viewModel = categoryListViewModel,
                onCategorySelected = { categoryId, categoryName ->
                    navController.navigate("foodlist/$categoryId/${Uri.encode(categoryName)}")
                },
                onBack = { navController.popBackStack() },
                onNavigateToDashboard = { navController.navigate("dashboard") }
            )
        }
        composable(
            "foodlist/{categoryId}/{categoryName}"
        ) { backStackEntry ->
            val context = LocalContext.current
            val db = AppDatabase.getDatabase(context)
            val foodDao = db.FoodDao()
            val repository = FoodItemsRepository(foodDao)
            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull() ?: 0
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            val factory = CategoriesFoodListViewModelFactory(repository, categoryId)
            val foodListViewModel: CategoriesFoodListViewModel = viewModel(factory = factory)

            CategoryFoodListScreen(
                viewModel = foodListViewModel,
                categoryId = categoryId,
                categoryName = categoryName,
                onBack = { navController.popBackStack() }
            )
        }
        composable("users") {
            val context = LocalContext.current
            val db = AppDatabase.getDatabase(context)
            val userDao = db.UserDao()
            val userRepository = UserRepository(userDao)
            val userManagementNavController = rememberNavController()

            androidx.navigation.compose.NavHost(
                navController = userManagementNavController,
                startDestination = "user_role_selection"
            ) {
                userManagementNavGraph(
                    navController = userManagementNavController,
                    userListViewModelFactoryProvider = { role ->
                        UserListViewModel.Factory(userRepository, role)
                    }
                )
            }
        }

        composable("shifts") {
            val context = LocalContext.current
            val db = AppDatabase.getDatabase(context)
            val shiftDao = db.ShiftDao()
            val shiftRepository = ShiftRepository(shiftDao)
            val shiftListViewModelFactory = ShiftListViewModel.Factory(shiftRepository)

            ShiftManagementHomeScreen(
                onBack = { navController.popBackStack() },
                shiftListViewModelFactory = shiftListViewModelFactory
            )
        }
        // ... other composables ...
    }
}