package com.example.catapi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catapi.ui.screens.detail.CatDetailScreen
import com.example.catapi.ui.screens.list.CatListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "list") {

        // Screen 1: The List
        composable("list") {
            CatListScreen(navController = navController)
        }

        // Screen 2: The Details
        composable(
            route = "detail?imageId={imageId}",
            arguments = listOf(navArgument("imageId") { type = NavType.StringType })
        ) {
            // The CatDetailViewModel will automatically get this "imageId"
            CatDetailScreen()
        }
    }
}