package com.example.tugasakhirmobel.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen(
    onLogoutClick: () -> Unit,
    onPingClick: () -> Unit // Tambahan parameter untuk fungsi Ping Server
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Halaman Dummy",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Login Anda Telah Berhasil!",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(48.dp))

        // --- TUGAS 1: Tombol Test Koneksi API ---
        Button(
            onClick = onPingClick,
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text(text = "Test Koneksi API")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol untuk kembali (Logout)
        Button(
            onClick = onLogoutClick,
            modifier = Modifier.fillMaxWidth(0.6f),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text(text = "Keluar / Logout")
        }
    }
}