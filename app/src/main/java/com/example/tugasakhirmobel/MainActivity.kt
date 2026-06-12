package com.example.tugasakhirmobel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
// 👇 1. IMPORT SPLASH SCREEN
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.tugasakhirmobel.ui.navigation.SetupNavGraph
import com.example.tugasakhirmobel.ui.screens.auth.AuthViewModel
import com.example.tugasakhirmobel.ui.theme.TugasAkhirMobelTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // 👇 2. PASANG SPLASH SCREEN DI SINI (Wajib sebelum super.onCreate)
        installSplashScreen()

        super.onCreate(savedInstanceState)

        // 👇 3. JURUS NUKLIR: MATIKAN BAR JUDUL (TugasAkhirMobel) DI SINI
        actionBar?.hide()

        setContent {
            TugasAkhirMobelTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = hiltViewModel()

                SetupNavGraph(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
        }
    }
}