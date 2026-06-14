package com.example.tugasakhirmobel.data.remote.model

import com.google.gson.annotations.SerializedName

data class LogResponse(
    val message: String,
    val data: List<LogModel>?
)

data class LogModel(
    val id: Int,
    val aksi: String,
    val waktu: String,
    @SerializedName("nama_admin") val namaAdmin: String
)