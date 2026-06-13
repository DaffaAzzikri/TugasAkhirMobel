package com.example.tugasakhirmobel.data.remote.model

import com.google.gson.annotations.SerializedName

// Wadah utama untuk menangkap response JSON
data class BarangListResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<BarangModel>
)
