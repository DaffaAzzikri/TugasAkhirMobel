package com.example.tugasakhirmobel.data.remote.api

import com.example.tugasakhirmobel.data.remote.model.UserModel
import retrofit2.Response
import retrofit2.http.GET

interface AuthApiService {
    // Mengambil data user yang sedang login dari PostgreSQL via Token Firebase
    @GET("api/v1/auth/me")
    suspend fun getCurrentUser(): Response<UserModel>
}
