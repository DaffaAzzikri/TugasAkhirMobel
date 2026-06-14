package com.example.tugasakhirmobel.data.remote.api

import com.example.tugasakhirmobel.data.remote.model.RiwayatResponse
import com.example.tugasakhirmobel.data.remote.model.RiwayatSummaryResponse
import com.example.tugasakhirmobel.data.remote.model.TransaksiRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RiwayatApiService {

    @GET("api/v1/riwayat")
    suspend fun getRiwayat(): Response<RiwayatResponse>

    @GET("api/v1/riwayat/summary")
    suspend fun getSummary(): Response<RiwayatSummaryResponse>

    @POST("api/v1/transaksi")
    suspend fun transaksi(@Body request: TransaksiRequest): Response<Map<String, Any>>
}
