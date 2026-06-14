package com.example.tugasakhirmobel.data.remote.api

import com.example.tugasakhirmobel.data.remote.model.DashboardResponse
import com.example.tugasakhirmobel.data.remote.model.BarangListResponse
import retrofit2.Response
import retrofit2.http.GET

interface DashboardApiService {

    @GET("api/v1/dashboard")
    suspend fun getDashboardSummary(): Response<DashboardResponse>

    @GET("api/v1/dashboard/perlu-perhatian")
    suspend fun getPerluPerhatian(): Response<BarangListResponse>
}