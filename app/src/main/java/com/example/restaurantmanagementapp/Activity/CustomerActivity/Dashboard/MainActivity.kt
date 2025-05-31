package com.example.restaurantmanagementapp.Activity.CustomerActivity.Dashboard

import CategoryListViewModelFactory
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.restaurantmanagementapp.Activity.BaseActivity
import com.example.restaurantmanagementapp.Activity.intro.IntroActivity
import com.example.restaurantmanagementapp.R
import com.example.restaurantmanagementapp.R_mDataBase.AppDatabase
import com.example.restaurantmanagementapp.Repository.CategoryRepository
import com.example.restaurantmanagementapp.ViewModel.CategoryListViewModel

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }

        // Register a callback for back press
        onBackPressedDispatcher.addCallback(this) {
            val intent = Intent(this@MainActivity, IntroActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}

@Composable
@Preview
fun MainScreen() {

    val context = LocalContext.current
    val appDatabase = AppDatabase.getDatabase(context)
    val categoryRepository = CategoryRepository(appDatabase.categoryDao())
    val factory = CategoryListViewModelFactory(categoryRepository)

    val categoryListViewModel: CategoryListViewModel = viewModel(factory = factory)
    CategoryGrid(viewModel = categoryListViewModel)
    val scaffoldState = rememberScaffoldState()

    Scaffold(bottomBar = { BottomMenu() },
        scaffoldState = scaffoldState
    ) {

        paddingValues ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
                .background(color = colorResource(R.color.black))
        ) {
            item {
                TopBar()
            }
            item {
                BannerCarousel()
            }
            item {
                Search()
            }
            item {
                CategoryGrid(viewModel = categoryListViewModel)
            }
        }
    }
}
