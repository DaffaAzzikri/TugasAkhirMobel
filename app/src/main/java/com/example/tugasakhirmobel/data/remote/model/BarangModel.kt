package com.example.tugasakhirmobel.data.remote.model

import com.google.gson.annotations.SerializedName

data class BarangModel(
    @SerializedName("id")
    val id: Int,

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

    @SerializedName("stok_minimum")
    val stokMinimum: Int,

    @SerializedName("image_url")
    val imageUrl: String
)