package com.example.tugasakhirmobel.data.remote.model

import com.google.gson.annotations.SerializedName

data class LogResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<LogModel>?
)

data class LogModel(
    @SerializedName("id") val id: Int,
    @SerializedName("aksi") val aksi: String,
    @SerializedName("nama_admin") val namaAdmin: String,
    @SerializedName("created_at") val createdAt: String // Sinkron dengan backend created_at
)
