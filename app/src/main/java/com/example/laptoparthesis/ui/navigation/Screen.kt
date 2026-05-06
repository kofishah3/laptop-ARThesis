package com.example.laptoparthesis.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Laptops : Screen("laptops")
    object Components : Screen("components")
    object LaptopDetail : Screen("laptop_detail/{laptopId}") {
        fun createRoute(laptopId: String) = "laptop_detail/$laptopId"
    }
    object ComponentDetail : Screen("component_detail/{componentId}") {
        fun createRoute(componentId: String) = "component_detail/$componentId"
    }
}
