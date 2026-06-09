package com.example.tugasakhirmobel.ui.navigation

sealed class ScreenRoutes(val route: String) {
    object Login : ScreenRoutes("login_screen")
    object Dashboard : ScreenRoutes("dashboard_screen")
}