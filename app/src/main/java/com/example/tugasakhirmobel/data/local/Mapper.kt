package com.example.tugasakhirmobel.data.local

import com.example.tugasakhirmobel.data.local.entity.BarangEntity
import com.example.tugasakhirmobel.data.remote.model.BarangModel

fun BarangModel.toEntity(): BarangEntity {
    return BarangEntity(
        id = this.id,
        namaBarang = this.namaBarang,
        sku = this.sku,
        kategori = this.kategori,
        harga = this.harga,
        supplier = this.supplier,
        stok = this.stok,
        stokMinimum = this.stokMinimum,
        imageUrl = this.imageUrl
    )
}

fun BarangEntity.toModel(): BarangModel {
    return BarangModel(
        id = this.id,
        namaBarang = this.namaBarang,
        sku = this.sku,
        kategori = this.kategori,
        harga = this.harga,
        supplier = this.supplier,
        stok = this.stok,
        stokMinimum = this.stokMinimum,
        imageUrl = this.imageUrl
    )
}
