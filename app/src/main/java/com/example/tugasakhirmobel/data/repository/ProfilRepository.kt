package com.example.tugasakhirmobel.data.repository

import com.example.tugasakhirmobel.data.remote.api.ProfilApiService
import com.example.tugasakhirmobel.data.remote.model.UserRequest
import com.example.tugasakhirmobel.data.remote.model.UserUpdateRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfilRepository @Inject constructor(
    private val apiService: ProfilApiService
) {
    suspend fun fetchSemuaUser() = apiService.getSemuaUser()

    suspend fun tambahUser(request: UserRequest) = apiService.tambahUser(request)

    suspend fun updateUser(id: Int, nama: String, isActive: Boolean) =
        apiService.updateUser(id, UserUpdateRequest(nama, isActive))

    suspend fun fetchSemuaLog() = apiService.getSemuaLog()
}
