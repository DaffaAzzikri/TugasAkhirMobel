package com.example.tugasakhirmobel.data.remote.model

import com.google.gson.annotations.SerializedName

// Wadah utama untuk menangkap response JSON
data class BarangListResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<BarangModel>
)

// Struktur data barang yang sudah diperbarui sesuai FastAPI
data class BarangModel(
    @SerializedName("id") val id: Int?,
    @SerializedName("nama_barang") val namaBarang: String,
    @SerializedName("sku") val sku: String,             // TAMBAHAN BARU
    @SerializedName("kategori") val kategori: String,   // TAMBAHAN BARU
    @SerializedName("harga") val harga: Int,            // TAMBAHAN BARU
    @SerializedName("supplier") val supplier: String,   // TAMBAHAN BARU
    @SerializedName("stok") val stok: Int,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("uid_pembuat") val uidPembuat: String?
)