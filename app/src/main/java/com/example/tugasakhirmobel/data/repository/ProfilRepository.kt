package com.example.tugasakhirmobel.data.repository

import com.example.tugasakhirmobel.data.remote.RetrofitClient
import com.example.tugasakhirmobel.data.remote.api.ProfilApiService
import com.example.tugasakhirmobel.data.remote.model.UserRequest
import com.example.tugasakhirmobel.data.remote.model.UserUpdateRequest
import javax.inject.Inject

class ProfilRepository @Inject constructor() {

    // Inisialisasi API Service langsung menggunakan RetrofitClient bawaan proyek
    private val apiService = RetrofitClient.instance.create(ProfilApiService::class.java)

    suspend fun fetchSemuaUser() = apiService.getSemuaUser()

    suspend fun tambahUser(request: UserRequest) = apiService.tambahUser(request)

    suspend fun updateUser(id: Int, nama: String, isActive: Boolean) =
        apiService.updateUser(id, UserUpdateRequest(nama, isActive))

    suspend fun fetchSemuaLog() = apiService.getSemuaLog()
}