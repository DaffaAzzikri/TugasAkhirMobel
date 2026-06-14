package com.example.tugasakhirmobel.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhirmobel.data.remote.model.BarangModel
import com.example.tugasakhirmobel.data.remote.model.DashboardData
import com.example.tugasakhirmobel.data.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DashboardState {
    object Loading : DashboardState()
    object Idle : DashboardState()
    data class Error(val message: String) : DashboardState()
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository
) : ViewModel() {

    private val _dashboardData = MutableStateFlow<DashboardData?>(null)
    val dashboardData: StateFlow<DashboardData?> = _dashboardData.asStateFlow()

    private val _perluPerhatian = MutableStateFlow<List<BarangModel>>(emptyList())
    val perluPerhatian: StateFlow<List<BarangModel>> = _perluPerhatian.asStateFlow()

    private val _state = MutableStateFlow<DashboardState>(DashboardState.Idle)
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _state.value = DashboardState.Loading
            try {
                val dashboardResponse = repository.getDashboardSummary()
                val perhatianResponse = repository.getPerluPerhatian()

                if (dashboardResponse.isSuccessful && perhatianResponse.isSuccessful) {
                    _dashboardData.value = dashboardResponse.body()?.data
                    _perluPerhatian.value = perhatianResponse.body()?.data ?: emptyList()
                    _state.value = DashboardState.Idle
                } else {
                    _state.value = DashboardState.Error("Gagal memuat data dari server")
                }
            } catch (e: Exception) {
                _state.value = DashboardState.Error(e.localizedMessage ?: "Terjadi kesalahan koneksi")
            }
        }
    }
}
