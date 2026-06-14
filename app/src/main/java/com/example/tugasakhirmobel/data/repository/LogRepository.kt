package com.example.tugasakhirmobel.data.repository

import com.example.tugasakhirmobel.data.remote.api.LogApiService
import com.example.tugasakhirmobel.data.remote.model.LogModel
import javax.inject.Inject

class LogRepository @Inject constructor(
    private val apiService: LogApiService
) {
    suspend fun getAllLogs(): List<LogModel> {
        return try {
            val response = apiService.getAllLogs()
            if (response.isSuccessful) response.body()?.data ?: emptyList()
            else emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}