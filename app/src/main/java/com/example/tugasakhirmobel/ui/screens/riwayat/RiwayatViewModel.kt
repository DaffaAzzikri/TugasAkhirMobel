package com.example.tugasakhirmobel.ui.screens.riwayat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhirmobel.data.remote.model.RiwayatModel
import com.example.tugasakhirmobel.data.remote.model.RiwayatSummaryData
import com.example.tugasakhirmobel.data.repository.RiwayatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class RiwayatState {
    object Idle : RiwayatState()
    object Loading : RiwayatState()
    data class Error(val message: String) : RiwayatState()
}

@HiltViewModel
class RiwayatViewModel @Inject constructor(
    private val repository: RiwayatRepository
) : ViewModel() {

    private val _riwayatList = MutableStateFlow<List<RiwayatModel>>(emptyList())
    val riwayatList: StateFlow<List<RiwayatModel>> = _riwayatList.asStateFlow()

    private val _summaryData = MutableStateFlow<RiwayatSummaryData?>(null)
    val summaryData: StateFlow<RiwayatSummaryData?> = _summaryData.asStateFlow()

    private val _state = MutableStateFlow<RiwayatState>(RiwayatState.Idle)
    val state: StateFlow<RiwayatState> = _state.asStateFlow()

    init {
        loadRiwayat()
    }

    fun loadRiwayat() {
        viewModelScope.launch {
            _state.value = RiwayatState.Loading
            try {
                val riwayatResponse = repository.getRiwayat()
                val summaryResponse = repository.getSummary()

                if (riwayatResponse.isSuccessful && summaryResponse.isSuccessful) {
                    _riwayatList.value = riwayatResponse.body()?.data ?: emptyList()
                    _summaryData.value = summaryResponse.body()?.data
                    _state.value = RiwayatState.Idle
                } else {
                    _state.value = RiwayatState.Error("Gagal mengambil data riwayat")
                }
            } catch (e: Exception) {
                _state.value = RiwayatState.Error(e.localizedMessage ?: "Terjadi kesalahan")
            }
        }
    }
}
