package com.example.tugasakhirmobel.data.remote.api

import com.example.tugasakhirmobel.data.remote.model.BarangRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BarangApiService {

    // Rute POST yang mengarah ke endpoint FastAPI Anda
    @POST("api/v1/barang")
    suspend fun createBarang(
        @Body request: BarangRequest
    ): Response<Map<String, Any>> // Menggunakan Map untuk menangkap respons JSON yang dinamis

}