package com.example.tugasakhirmobel.data.remote.model

import com.google.gson.annotations.SerializedName

data class RiwayatSummaryResponse(
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: RiwayatSummaryData
)

data class RiwayatSummaryData(
    @SerializedName("stok_masuk") val stokMasuk: Int,
    @SerializedName("stok_keluar") val stokKeluar: Int
)
