package com.example.tugasakhirmobel.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tugasakhirmobel.ui.screens.auth.AuthViewModel
import com.example.tugasakhirmobel.ui.screens.auth.LoginScreen
import com.example.tugasakhirmobel.ui.screens.dashboard.DashboardScreen
import com.example.tugasakhirmobel.ui.screens.barang.FormBarangScreen
import com.example.tugasakhirmobel.ui.screens.browse.BrowseScreen
import com.example.tugasakhirmobel.ui.screens.riwayat.RiwayatScreen
import com.example.tugasakhirmobel.ui.screens.profil.ProfileScreen
import com.example.tugasakhirmobel.ui.screens.profil.ManajemenAkunScreen
import com.example.tugasakhirmobel.ui.screens.log.LogScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tugasakhirmobel.data.remote.model.BarangModel
import com.example.tugasakhirmobel.ui.screens.barang.BarangViewModel

// 1. Data Class untuk menyimpan informasi menu Navbar
data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    // Shared ViewModel for Barang CRUD operations
    val barangViewModel: BarangViewModel = hiltViewModel()

    // Memantau posisi halaman saat ini untuk menentukan warna ikon aktif
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Daftar menu yang akan ditampilkan di Navbar
    val bottomNavItems = listOf(
        BottomNavItem("Dashboard", Icons.Default.Dashboard, ScreenRoutes.Dashboard.route),
        BottomNavItem("Barang", Icons.Default.Inventory, ScreenRoutes.Browse.route),
        BottomNavItem("Riwayat", Icons.Default.SwapHoriz, ScreenRoutes.Riwayat.route),
        BottomNavItem("Log", Icons.Default.Assignment, ScreenRoutes.Log.route),
        BottomNavItem("Profil", Icons.Default.Person, ScreenRoutes.Profil.route)
    )

    // Daftar halaman yang BOLEH menampilkan Navbar (Login dan Form Tambah tidak boleh)
    val showBottomNav = currentRoute in listOf(
        ScreenRoutes.Dashboard.route,
        ScreenRoutes.Browse.route,
        ScreenRoutes.Riwayat.route,
        ScreenRoutes.Log.route,
        ScreenRoutes.Profil.route
    )

    Scaffold(
        bottomBar = {
            // Hanya tampilkan Navbar jika showBottomNav bernilai true
            if (showBottomNav) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp // Memberikan efek bayangan lembut di atas navbar
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = currentRoute == item.route
                        NavigationBarItem(
                            icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                            label = { Text(text = item.title) },
                            selected = isSelected,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF0D47A1), // Biru gelap untuk ikon aktif (sesuai referensi)
                                selectedTextColor = Color(0xFF0D47A1),
                                indicatorColor = Color(0xFFE3F2FD), // Biru sangat muda untuk latar belakang ikon aktif
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            ),
                            onClick = {
                                // Logika untuk pindah halaman lewat Navbar tanpa menumpuk riwayat berlebihan
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // NavHost sekarang dibungkus dengan modifier.padding agar konten tidak tertutup Navbar
        NavHost(
            navController = navController,
            startDestination = ScreenRoutes.Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // --- RUTE LAYAR LOGIN ---
            composable(route = ScreenRoutes.Login.route) {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                        navController.navigate(ScreenRoutes.Dashboard.route) {
                            popUpTo(ScreenRoutes.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            // --- RUTE LAYAR DASHBOARD ---
            composable(route = ScreenRoutes.Dashboard.route) {
                DashboardScreen(
                    onLogoutClick = {
                        navController.navigate(ScreenRoutes.Login.route) {
                            popUpTo(0) // Membersihkan semua riwayat saat logout
                        }
                    },
                    onPingClick = { authViewModel.testKoneksiKeFastAPI() },
                    onAddProductClick = { 
                        barangViewModel.resetState()
                        navController.navigate(ScreenRoutes.ProductForm.route) 
                    },
                    onBrowseClick = { navController.navigate(ScreenRoutes.Browse.route) }
                )
            }

            // --- RUTE LAYAR TAMBAH/EDIT PRODUK ---
            composable(route = ScreenRoutes.ProductForm.route) {
                FormBarangScreen(
                    viewModel = barangViewModel,
                    onCloseClick = { navController.popBackStack() }
                )
            }

            // --- RUTE LAYAR BROWSE (DAFTAR BARANG) ---
            composable(route = ScreenRoutes.Browse.route) {
                BrowseScreen(
                    onAddClick = { 
                        barangViewModel.resetState()
                        navController.navigate(ScreenRoutes.ProductForm.route) 
                    },
                    onEditClick = { barang ->
                        barangViewModel.itemToEdit = barang
                        navController.navigate(ScreenRoutes.ProductForm.route)
                    },
                    viewModel = barangViewModel
                )
            }
            composable(route = ScreenRoutes.Riwayat.route) {
                RiwayatScreen()
            }
            composable(route = ScreenRoutes.Log.route) {
                LogScreen()
            }
            composable(route = ScreenRoutes.Profil.route) {
                ProfileScreen(
                    onLogoutClick = {
                        navController.navigate(ScreenRoutes.Login.route) {
                            popUpTo(0)
                        }
                    },
                    onManageAccountClick = {
                        navController.navigate("manajemen_akun") // Rute baru
                    }
                )
            }
            composable(route = "manajemen_akun") {
                ManajemenAkunScreen(onBackClick = { navController.popBackStack() })
            }
        }
    }
}
