package com.example.laptoparthesis.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.laptoparthesis.ui.screens.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Laptops.route) {
            LaptopListScreen(onLaptopClick = { laptopId ->
                navController.navigate(Screen.LaptopDetail.createRoute(laptopId))
            })
        }
        composable(Screen.Components.route) {
            ComponentListScreen(onComponentClick = { componentId ->
                navController.navigate(Screen.ComponentDetail.createRoute(componentId))
            })
        }
        composable(Screen.LaptopDetail.route) { backStackEntry ->
            val laptopId = backStackEntry.arguments?.getString("laptopId") ?: ""
            DetailScreen(id = laptopId, type = "Laptop Model")
        }
        composable(Screen.ComponentDetail.route) { backStackEntry ->
            val componentId = backStackEntry.arguments?.getString("componentId") ?: ""
            DetailScreen(id = componentId, type = "Hardware Component")
        }
    }
}
