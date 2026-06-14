package com.example.tugasakhirmobel.ui.screens.profil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhirmobel.data.remote.model.LogModel
import com.example.tugasakhirmobel.data.remote.model.UserModel
import com.example.tugasakhirmobel.data.remote.model.UserRequest
import com.example.tugasakhirmobel.data.repository.ProfilRepository
import com.example.tugasakhirmobel.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ProfilState {
    object Idle : ProfilState()
    object Loading : ProfilState()
    object Success : ProfilState()
    data class Error(val message: String) : ProfilState()
}

@HiltViewModel
class ProfilViewModel @Inject constructor(
    private val repository: ProfilRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ProfilState>(ProfilState.Idle)
    val state: StateFlow<ProfilState> = _state.asStateFlow()

    private val _profileState = MutableStateFlow<ProfilState>(ProfilState.Idle)
    val profileState: StateFlow<ProfilState> = _profileState.asStateFlow()

    private val _currentUser = MutableStateFlow<UserModel?>(null)
    val currentUser: StateFlow<UserModel?> = _currentUser.asStateFlow()

    private val _userList = MutableStateFlow<List<UserModel>>(emptyList())
    val userList: StateFlow<List<UserModel>> = _userList.asStateFlow()

    private val _logList = MutableStateFlow<List<LogModel>>(emptyList())
    val logList: StateFlow<List<LogModel>> = _logList.asStateFlow()

    fun loadCurrentUser() {
        _profileState.value = ProfilState.Loading
        viewModelScope.launch {
            try {
                val response = userRepository.getCurrentUser()
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null && !user.nama.isNullOrBlank() && !user.email.isNullOrBlank()) {
                        _currentUser.value = user
                        _profileState.value = ProfilState.Success
                    } else {
                        _profileState.value = ProfilState.Error("Data profil tidak lengkap dari server.")
                    }
                } else {
                    _profileState.value = ProfilState.Error("Gagal memuat profil. Silakan coba lagi.")
                }
            } catch (e: Exception) {
                _profileState.value = ProfilState.Error(
                    "Tidak dapat terhubung ke server. Periksa koneksi internet Anda."
                )
            }
        }
    }

    fun loadUsers() {
        _state.value = ProfilState.Loading
        viewModelScope.launch {
            try {
                val response = repository.fetchSemuaUser()
                if (response.isSuccessful) {
                    _userList.value = response.body()?.data ?: emptyList()
                    _state.value = ProfilState.Success
                } else {
                    _state.value = ProfilState.Error("Gagal mengambil data akun")
                }
            } catch (e: Exception) {
                _state.value = ProfilState.Error(e.localizedMessage ?: "Error jaringan")
            }
        }
    }

    fun tambahUserBaru(nama: String, email: String, role: String) {
        if (nama.isBlank() || email.isBlank()) {
            _state.value = ProfilState.Error("Nama dan Email wajib diisi!")
            return
        }
        _state.value = ProfilState.Loading
        viewModelScope.launch {
            try {
                val response = repository.tambahUser(UserRequest(nama, email, role))
                if (response.isSuccessful) {
                    loadUsers() // Refresh list otomatis
                } else {
                    _state.value = ProfilState.Error("Gagal menambah akun")
                }
            } catch (e: Exception) {
                _state.value = ProfilState.Error(e.localizedMessage ?: "Error")
            }
        }
    }

    fun editUser(id: Int, nama: String, isActive: Boolean) {
        _state.value = ProfilState.Loading
        viewModelScope.launch {
            try {
                val response = repository.updateUser(id, nama, isActive)
                if (response.isSuccessful) {
                    loadUsers() // Refresh UI agar nama baru dan toggle switch terupdate dari server
                } else {
                    _state.value = ProfilState.Error("Gagal memperbarui pengguna")
                }
            } catch (e: Exception) {
                _state.value = ProfilState.Error(e.localizedMessage ?: "Gagal ubah data")
            }
        }
    }
}