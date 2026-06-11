package com.example.tugasakhirmobel.data.repository

import android.net.Uri
import com.example.tugasakhirmobel.data.remote.api.BarangApiService
import com.example.tugasakhirmobel.data.remote.RetrofitClient
import com.example.tugasakhirmobel.data.remote.media.CloudinaryHelper
import com.example.tugasakhirmobel.data.remote.model.BarangRequest
// 1. TAMBAHKAN IMPORT INJECT & SINGLETON DI SINI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Membuat repository ini bersifat tunggal di seluruh aplikasi (hemat memori)
class BarangRepository @Inject constructor() { // 2. PERBAIKAN: Tambahkan @Inject constructor()

    // Panggil mesin Retrofit untuk mengakses endpoint FastAPI
    private val apiService = RetrofitClient.instance.create(BarangApiService::class.java)

    /**
     * Fungsi utama untuk mengunggah foto dan menyimpan data ke server.
     * Menggunakan Result pembungkus agar UI mudah menangkap sukses/gagalnya.
     */
    suspend fun tambahBarang(nama: String, stok: Int, imageUri: Uri): Result<String> {
        return try {
            // 1. Serahkan foto ke Cloudinary, tunggu sampai dapat URL publiknya
            val imageUrl = CloudinaryHelper.uploadImage(imageUri)

            // 2. Bungkus ke dalam DTO (Data Transfer Object) yang sesuai dengan FastAPI
            val requestData = BarangRequest(
                namaBarang = nama,
                stok = stok,
                imageUrl = imageUrl
            )

            // 3. Tembak ke server
            val response = apiService.createBarang(requestData)

            if (response.isSuccessful) {
                Result.success("Berhasil! Barang telah ditambahkan ke database.")
            } else {
                // Tangkap error dari FastAPI (misal Error 422 Unprocessable Entity)
                Result.failure(Exception("Gagal menyimpan ke server: Kode ${response.code()}"))
            }
        } catch (e: Exception) {
            // Tangkap error jaringan (internet putus, server mati, dll)
            Result.failure(e)
        }
    }
}