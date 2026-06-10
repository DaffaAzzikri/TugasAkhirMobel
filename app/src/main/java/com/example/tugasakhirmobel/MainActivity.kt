package com.example.tugasakhirmobel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.tugasakhirmobel.ui.navigation.SetupNavGraph
import com.example.tugasakhirmobel.ui.screens.auth.AuthViewModel
import com.example.tugasakhirmobel.ui.theme.TugasAkhirMobelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TugasAkhirMobelTheme { // Nama tema mungkin berbeda tergantung nama proyek Anda
                // Inisialisasi pengontrol rute dan otak logika
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()

                // Pasang jalan tolnya
                SetupNavGraph(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
        }
    }
}