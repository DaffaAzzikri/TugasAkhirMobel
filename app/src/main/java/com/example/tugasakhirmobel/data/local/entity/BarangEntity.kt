package com.example.tugasakhirmobel.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "barang")
data class BarangEntity(
    @PrimaryKey
    val id: Int,
    val namaBarang: String,
    val sku: String,
    val kategori: String,
    val harga: Int,
    val supplier: String,
    val stok: Int,
    val stokMinimum: Int,
    val imageUrl: String
)
