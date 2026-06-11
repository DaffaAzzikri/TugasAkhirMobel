package com.example.tugasakhirmobel.data.remote.model

import com.google.gson.annotations.SerializedName

data class BarangRequest(
    @SerializedName("nama_barang")
    val namaBarang: String,

    @SerializedName("stok")
    val stok: Int,

    @SerializedName("image_url")
    val imageUrl: String
)