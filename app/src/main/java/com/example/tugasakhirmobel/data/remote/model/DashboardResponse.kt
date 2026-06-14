package com.example.tugasakhirmobel.data.remote.model

import com.google.gson.annotations.SerializedName

data class DashboardResponse(
    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: DashboardData
)

data class DashboardData(
    @SerializedName("total_produk")
    val totalProduk: Int,

    @SerializedName("tersedia")
    val tersedia: Int,

    @SerializedName("stok_rendah")
    val stokRendah: Int,

    @SerializedName("habis")
    val habis: Int,

    @SerializedName("total_nilai_inventaris")
    val totalNilaiInventaris: Long
)