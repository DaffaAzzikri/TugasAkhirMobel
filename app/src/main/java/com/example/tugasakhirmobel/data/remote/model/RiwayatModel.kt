package com.example.tugasakhirmobel.data.remote.model

import com.google.gson.annotations.SerializedName

data class RiwayatModel(
    @SerializedName("id") val id: Int,
    @SerializedName("barang_id") val barangId: Int,
    @SerializedName("nama_barang") val namaBarang: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("jenis") val jenis: String,
    @SerializedName("jumlah") val jumlah: Int,
    @SerializedName("keterangan") val keterangan: String,
    @SerializedName("admin") val admin: String,
    @SerializedName("tanggal") val tanggal: String
)
