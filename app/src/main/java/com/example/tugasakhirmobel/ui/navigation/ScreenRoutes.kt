package com.example.tugasakhirmobel.ui.navigation

sealed class ScreenRoutes(val route: String) {
    object Login : ScreenRoutes("login_screen")
    object Dashboard : ScreenRoutes("dashboard_screen")
    object ProductForm : ScreenRoutes("product_form_screen")
    object Browse : ScreenRoutes("browse_screen")
    object Riwayat : ScreenRoutes("riwayat_screen")
    object Log : ScreenRoutes("log_screen")
    object Profil : ScreenRoutes("profil_screen")
}