package com.example.tugasakhirmobel.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tugasakhirmobel.ui.screens.auth.AuthViewModel
import com.example.tugasakhirmobel.ui.screens.auth.LoginScreen
import com.example.tugasakhirmobel.ui.screens.dashboard.DashboardScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.Login.route
    ) {
        // Rute Layar Login
        composable(route = ScreenRoutes.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    // Pindah ke Dashboard & hapus layar login dari riwayat (backstack)
                    navController.navigate(ScreenRoutes.Dashboard.route) {
                        popUpTo(ScreenRoutes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Rute Layar Dashboard
        composable(route = ScreenRoutes.Dashboard.route) {
            DashboardScreen(
                onLogoutClick = {
                    // Navigate back to login or perform logout logic
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                },
                onPingClick = {
                    // Call your API test function here
                    println("Ping button clicked")
                }
            )
        }
    }
}