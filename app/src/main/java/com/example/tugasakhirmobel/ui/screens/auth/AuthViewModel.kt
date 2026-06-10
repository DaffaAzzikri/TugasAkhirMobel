package com.example.tugasakhirmobel.ui.screens.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// 1. Membungkus status layar (State) agar UI Compose mudah bereaksi
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {

    // Inisialisasi Firebase Auth
    private val auth = FirebaseAuth.getInstance()

    // 2. Tempat menyimpan status saat ini
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // 3. Fungsi utama yang akan dipanggil saat tombol "Masuk" ditekan
    fun login(email: String, sandi: String) {
        // Validasi dasar
        if (email.isBlank() || sandi.isBlank()) {
            _authState.value = AuthState.Error("Email dan kata sandi tidak boleh kosong!")
            return
        }

        // Ubah status menjadi loading (UI bisa memunculkan indikator berputar)
        _authState.value = AuthState.Loading

        // Tembak data ke Firebase
        auth.signInWithEmailAndPassword(email, sandi)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Berhasil! Sesi langsung otomatis dikunci oleh Firebase di HP
                    _authState.value = AuthState.Success
                } else {
                    // Gagal, tangkap pesan error dari Firebase
                    val pesanError = task.exception?.localizedMessage ?: "Terjadi kesalahan saat login"
                    _authState.value = AuthState.Error(pesanError)
                }
            }
    }

    // Fungsi untuk mereset status (berguna jika user sudah menutup pop-up error)
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}