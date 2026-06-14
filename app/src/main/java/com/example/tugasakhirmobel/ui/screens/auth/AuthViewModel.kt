package com.example.tugasakhirmobel.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhirmobel.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(email: String, sandi: String) {
        if (email.isBlank() || sandi.isBlank()) {
            _authState.value = AuthState.Error("Email dan kata sandi tidak boleh kosong!")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            // 1. Validasi Firebase
            val hasilFirebase = repository.doLogin(email, sandi)

            hasilFirebase.fold(
                onSuccess = { uid ->
                    // 2. SINKRONISASI DATABASE (Cek is_active di PostgreSQL)
                    try {
                        val response = repository.checkUserActiveStatus()
                        if (response.isSuccessful) {
                            // Jika Backend OK, berarti user Aktif
                            _authState.value = AuthState.Success
                        } else {
                            // User mungkin is_active = false
                            _authState.value = AuthState.Error("Akun Anda dinonaktifkan oleh Super Admin.")
                        }
                    } catch (e: Exception) {
                        _authState.value = AuthState.Error("Gagal verifikasi database: ${e.message}")
                    }
                },
                onFailure = { error ->
                    _authState.value = AuthState.Error("Email atau password salah.")
                }
            )
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
