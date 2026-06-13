package com.example.tugasakhirmobel.data.remote.model

import com.google.gson.annotations.SerializedName

data class BarangRequest(
    @SerializedName("nama_barang")
    val namaBarang: String,

    @SerializedName("sku")
    val sku: String,

    @SerializedName("kategori")
    val kategori: String,

    @SerializedName("harga")
    val harga: Int,

    @SerializedName("supplier")
    val supplier: String,

    @SerializedName("stok")
    val stok: Int,

    @SerializedName("image_url")
    val imageUrl: String
)
