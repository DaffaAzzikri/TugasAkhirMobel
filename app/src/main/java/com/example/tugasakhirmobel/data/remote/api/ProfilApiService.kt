package com.example.tugasakhirmobel.data.remote.api

import com.example.tugasakhirmobel.data.remote.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfilApiService {
    @GET("api/v1/users")
    suspend fun getSemuaUser(): Response<UserResponse>

    @POST("api/v1/users")
    suspend fun tambahUser(@Body request: UserRequest): Response<SingleUserResponse>

    @PUT("api/v1/users/{id}")
    suspend fun updateUser(
        @Path("id") userId: Int,
        @Body request: UserUpdateRequest
    ): Response<SingleUserResponse>

    @GET("api/v1/logs")
    suspend fun getSemuaLog(): Response<LogResponse>
}