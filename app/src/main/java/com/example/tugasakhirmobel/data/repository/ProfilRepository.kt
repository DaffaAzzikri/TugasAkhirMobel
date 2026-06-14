package com.example.tugasakhirmobel.data.repository

import com.example.tugasakhirmobel.data.remote.RetrofitClient
import com.example.tugasakhirmobel.data.remote.api.ProfilApiService
import com.example.tugasakhirmobel.data.remote.model.UserRequest
import com.example.tugasakhirmobel.data.remote.model.UserStatusRequest
import javax.inject.Inject

class ProfilRepository @Inject constructor() {

    // Inisialisasi API Service langsung menggunakan RetrofitClient bawaan proyek
    private val apiService = RetrofitClient.instance.create(ProfilApiService::class.java)

    suspend fun fetchSemuaUser() = apiService.getSemuaUser()

    suspend fun tambahUser(request: UserRequest) = apiService.tambahUser(request)

    suspend fun updateStatus(id: Int, isActive: Boolean) =
        apiService.updateStatusUser(id, UserStatusRequest(isActive))

    suspend fun fetchSemuaLog() = apiService.getSemuaLog()
}