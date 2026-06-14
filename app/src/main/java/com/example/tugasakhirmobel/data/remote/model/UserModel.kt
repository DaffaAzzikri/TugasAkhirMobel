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
    val id: Int = 0,
    @SerializedName("firebase_uid") val firebaseUid: String? = null,
    val nama: String? = null,
    val email: String? = null,
    val role: String? = null,
    @SerializedName("is_active") val isActive: Boolean = true,
    @SerializedName("created_at") val createdAt: String? = null
)

// Model untuk mengirim data saat Tambah User
data class UserRequest(
    val nama: String,
    val email: String,
    val role: String
)

// Model untuk mengubah status Aktif/Nonaktif
data class UserUpdateRequest(
    val nama: String,
    @SerializedName("is_active") val isActive: Boolean
)
