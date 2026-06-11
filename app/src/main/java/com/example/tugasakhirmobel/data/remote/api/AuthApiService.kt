package com.example.tugasakhirmobel.data.remote.api

import retrofit2.Response
import retrofit2.http.GET

interface AuthApiService {

    // Rute endpoint FastAPI Anda (tanpa garis miring di awal karena sudah ada di BASE_URL)
    @GET("api/v1/auth/me")
    suspend fun pingServer(): Response<Map<String, String>>

}