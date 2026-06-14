package com.example.tugasakhirmobel.ui.screens.log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhirmobel.data.remote.model.LogModel
import com.example.tugasakhirmobel.data.repository.LogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogViewModel @Inject constructor(
    private val repository: LogRepository // Perbaikan: Gunakan Repository, bukan ApiService
) : ViewModel() {
    private val _logList = MutableStateFlow<List<LogModel>>(emptyList())
    val logList: StateFlow<List<LogModel>> = _logList

    init {
        fetchLogs()
    }

    fun fetchLogs() {
        viewModelScope.launch {
            try {
                // Panggil repository
                val logs = repository.getAllLogs()
                _logList.value = logs
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}