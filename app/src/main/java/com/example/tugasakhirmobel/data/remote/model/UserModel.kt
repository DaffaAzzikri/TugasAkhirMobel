package com.example.tugasakhirmobel.data.remote.model

import com.google.gson.annotations.SerializedName

// Model untuk menerima data dari Backend
data class UserResponse(
    val message: String,
    val data: List<UserModel>?
)

data class SingleUserResponse(
    val message: String,
    val data: UserModel?
)

data class UserModel(
    val id: Int,
    val nama: String,
    val email: String,
    val role: String,
    @SerializedName("is_active") val isActive: Boolean
)

// Model untuk mengirim data saat Tambah User
data class UserRequest(
    val nama: String,
    val email: String,
    val role: String
)

// Model untuk mengubah status Aktif/Nonaktif
data class UserStatusRequest(
    @SerializedName("is_active") val isActive: Boolean
)