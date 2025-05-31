package com.example.restaurantmanagementapp.Activity.AdminActivity.ManageAdminNav

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.restaurantmanagementapp.Activity.AdminActivity.ManageUser.UserRoleSelectionScreen
import com.example.restaurantmanagementapp.Activity.AdminActivity.ManageUser.UserListScreen
import com.example.restaurantmanagementapp.ViewModel.UserListViewModel

fun NavGraphBuilder.userManagementNavGraph(
    navController: NavController,
    userListViewModelFactoryProvider: (String) -> UserListViewModel.Factory
) {
    composable("user_role_selection") {
        UserRoleSelectionScreen(
            onRoleSelected = { role ->
                navController.navigate("user_list/$role")
            }
        )
    }
    composable("user_list/{role}") { backStackEntry ->
        val role = backStackEntry.arguments?.getString("role") ?: "employee"
        val viewModelFactory = userListViewModelFactoryProvider(role)
        val viewModel: UserListViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = viewModelFactory)
        UserListScreen(
            viewModel = viewModel,
            role = role,
            onBack = { navController.popBackStack() }
        )
    }
}