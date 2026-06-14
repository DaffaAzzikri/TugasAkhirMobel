package com.example.tugasakhirmobel.data.repository

import com.example.tugasakhirmobel.data.remote.RetrofitClient
import com.example.tugasakhirmobel.data.remote.api.DashboardApiService
import com.example.tugasakhirmobel.data.remote.model.BarangListResponse
import com.example.tugasakhirmobel.data.remote.model.DashboardResponse
import com.example.tugasakhirmobel.data.remote.model.RiwayatResponse
import com.example.tugasakhirmobel.data.remote.model.UserModel
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepository @Inject constructor(
    private val userRepository: UserRepository
) {

    private val apiService = RetrofitClient.instance.create(DashboardApiService::class.java)

    suspend fun getDashboardSummary(): Response<DashboardResponse> =
        apiService.getDashboardSummary()

    suspend fun getPerluPerhatian(): Response<BarangListResponse> =
        apiService.getPerluPerhatian()

    suspend fun getPergerakanTerakhir(): Response<RiwayatResponse> =
        apiService.getPergerakanTerakhir()

    suspend fun getCurrentUserProfile(): Response<UserModel> =
        userRepository.getCurrentUser()
}
