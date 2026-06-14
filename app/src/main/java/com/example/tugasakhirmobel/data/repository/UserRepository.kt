package com.example.tugasakhirmobel.data.repository

import com.example.tugasakhirmobel.data.remote.api.AuthApiService
import com.example.tugasakhirmobel.data.remote.model.UserModel
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val authApiService: AuthApiService
) {
    suspend fun getCurrentUser(): Response<UserModel> =
        authApiService.getCurrentUser()
}
