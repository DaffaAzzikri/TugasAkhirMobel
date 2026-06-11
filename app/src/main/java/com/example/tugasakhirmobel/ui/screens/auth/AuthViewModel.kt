package com.example.tugasakhirmobel.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhirmobel.data.remote.RetrofitClient
import com.example.tugasakhirmobel.data.remote.api.AuthApiService
import com.example.tugasakhirmobel.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// Pembungkus status layar (tetap sama)
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel // <-- Beritahu Hilt bahwa ini adalah ViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository // <-- Hilt otomatis memasukkan ini!
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(email: String, sandi: String) {
        if (email.isBlank() || sandi.isBlank()) {
            _authState.value = AuthState.Error("Email dan kata sandi tidak boleh kosong!")
            return
        }

        _authState.value = AuthState.Loading

        // Jalankan perintah ke Repositori di background thread
        viewModelScope.launch {
            val hasil = repository.doLogin(email, sandi)

            // Evaluasi hasil dari Repositori
            hasil.fold(
                onSuccess = { uid ->
                    _authState.value = AuthState.Success
                },
                onFailure = { error ->
                    val pesanError = error.localizedMessage ?: "Terjadi kesalahan saat login"
                    _authState.value = AuthState.Error(pesanError)
                }
            )
        }
        fun testKoneksiKeFastAPI() {
            viewModelScope.launch {
                try {
                    _authState.value = AuthState.Loading
                    val apiService = RetrofitClient.instance.create(AuthApiService::class.java)
                    val response = apiService.pingServer()

                    if (response.isSuccessful) {
                        println("🔥 PING BERHASIL: ${response.body()}")
                        _authState.value = AuthState.Success
                    } else {
                        _authState.value = AuthState.Error("Gagal: ${response.code()}")
                    }
                } catch (e: Exception) {
                    _authState.value = AuthState.Error("Koneksi Error: ${e.message}")
                }
            }

        }

        fun resetState() {
            _authState.value = AuthState.Idle
        }
    }
}