package com.example.tugasakhirmobel.data.remote.model

import com.google.gson.annotations.SerializedName

data class TransaksiRequest(
    @SerializedName("barang_id") val barangId: Int,
    @SerializedName("jenis") val jenis: String,
    @SerializedName("jumlah") val jumlah: Int,
    @SerializedName("keterangan") val keterangan: String
)
