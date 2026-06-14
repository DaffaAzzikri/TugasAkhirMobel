package com.example.tugasakhirmobel.data.repository

import com.example.tugasakhirmobel.data.remote.RetrofitClient
import com.example.tugasakhirmobel.data.remote.api.DashboardApiService
import com.example.tugasakhirmobel.data.remote.model.BarangListResponse
import com.example.tugasakhirmobel.data.remote.model.DashboardResponse
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepository @Inject constructor() {

    private val apiService =
        RetrofitClient.instance.create(DashboardApiService::class.java)

    suspend fun getDashboardSummary(): Response<DashboardResponse> =
        apiService.getDashboardSummary()

    suspend fun getPerluPerhatian(): Response<BarangListResponse> =
        apiService.getPerluPerhatian()
}