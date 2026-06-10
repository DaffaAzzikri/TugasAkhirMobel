package com.example.tugasakhirmobel.ui.screens.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// State untuk memantau status login di UI
sealed interface AuthState {
    object Idle : AuthState
    object Loading : AuthState
    object Success : AuthState
    data class Error(val message: String) : AuthState
}

class AuthViewModel : ViewModel() {

    // Mengambil instans FirebaseAuth sesuai alur kerja
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Backing property untuk menampung status otentikasi
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        // Validasi dasar agar tidak melakukan request kosong
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email dan password tidak boleh kosong")
            return
        }

        // Mengubah status menjadi loading saat proses dimulai
        _authState.value = AuthState.Loading

        // Memanggil fungsi bawaan Firebase SDK
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Jika sukses, sesi pengguna otomatis terkunci di HP
                    _authState.value = AuthState.Success
                } else {
                    // Jika gagal, ambil pesan error dari Firebase
                    val errorMessage = task.exception?.message ?: "Login gagal, periksa kembali akun Anda."
                    _authState.value = AuthState.Error(errorMessage)
                }
            }
    }

    // Fungsi untuk mereset status setelah error ditangani
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}