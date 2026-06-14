package com.example.tugasakhirmobel.data.remote.model

import com.google.gson.annotations.SerializedName

data class RiwayatResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<RiwayatModel>
)
