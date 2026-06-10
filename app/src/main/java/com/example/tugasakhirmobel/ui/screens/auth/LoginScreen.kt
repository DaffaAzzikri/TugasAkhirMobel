package com.example.tugasakhirmobel.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel // Diperlukan untuk inisialisasi ViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel = viewModel(), // Menghubungkan ke AuthViewModel
    onLoginSuccess: () -> Unit // Aksi callback ketika Firebase mengonfirmasi sukses [cite: 400, 405]
) {
    // State untuk menampung input pengguna [cite: 403]
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Mengamati perubahan status otentikasi dari ViewModel
    val authState by viewModel.authState.collectAsState()

    // Menangani efek samping perpindahan halaman ketika login sukses [cite: 258, 405]
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo / Icon Placeholder (Sesuai Wireframe) [cite: 109, 111]
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Logo Mobin",
            modifier = Modifier.size(100.dp),
            tint = MaterialTheme.colorScheme.primary // Sudah diperbaiki dari 'theme' menjadi 'tint'
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Judul Aplikasi [cite: 113, 114]
        Text(
            text = "Mobin",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Inventory Android",
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Input Email [cite: 403]
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            enabled = authState !is AuthState.Loading // Mengunci input saat loading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input Password [cite: 403]
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            enabled = authState !is AuthState.Loading // Mengunci input saat loading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Indikator Loading (Muncul saat mencocokkan akun ke Firebase)
        if (authState is AuthState.Loading) {
            CircularProgressIndicator(modifier = Modifier.padding(vertical = 8.dp))
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Pesan Galat/Error (Sesuai Acceptance Criteria PRD jika login gagal)
        if (authState is AuthState.Error) {
            Text(
                text = (authState as AuthState.Error).message,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tombol Masuk [cite: 403]
        Button(
            onClick = { viewModel.login(email, password) }, // Mengeksekusi fungsi auth di ViewModel [cite: 404]
            enabled = authState !is AuthState.Loading,      // Tombol mati otomatis saat proses loading berjalan
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "Masuk", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Footer Register (Sesuai Wireframe) [cite: 127]
        TextButton(onClick = { /* Navigasi ke Register jika ada */ }) {
            Text(text = "If you don't have account, Register here")
        }
    }
}
