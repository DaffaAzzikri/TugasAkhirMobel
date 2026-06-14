package com.example.tugasakhirmobel.data.remote.api

import com.example.tugasakhirmobel.data.remote.model.LogResponse
import retrofit2.Response
import retrofit2.http.GET

interface LogApiService {
    @GET("api/v1/logs") // Sesuaikan endpoint dengan API Daffa
    suspend fun getAllLogs(): Response<LogResponse>
}