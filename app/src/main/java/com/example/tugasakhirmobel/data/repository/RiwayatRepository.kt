package com.example.tugasakhirmobel.data.repository

import com.example.tugasakhirmobel.data.remote.RetrofitClient
import com.example.tugasakhirmobel.data.remote.api.RiwayatApiService
import com.example.tugasakhirmobel.data.remote.model.RiwayatResponse
import com.example.tugasakhirmobel.data.remote.model.RiwayatSummaryResponse
import com.example.tugasakhirmobel.data.remote.model.TransaksiRequest
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RiwayatRepository @Inject constructor() {

    private val apiService = RetrofitClient.instance.create(RiwayatApiService::class.java)

    suspend fun getRiwayat(): Response<RiwayatResponse> {
        return apiService.getRiwayat()
    }

    suspend fun getSummary(): Response<RiwayatSummaryResponse> {
        return apiService.getSummary()
    }

    suspend fun transaksi(request: TransaksiRequest): Response<Map<String, Any>> {
        return apiService.transaksi(request)
    }
}
