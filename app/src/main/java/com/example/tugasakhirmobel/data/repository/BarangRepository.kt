package com.example.tugasakhirmobel.data.repository

import com.example.tugasakhirmobel.data.remote.api.BarangApiService
import com.example.tugasakhirmobel.data.remote.RetrofitClient
import com.example.tugasakhirmobel.data.remote.model.BarangListResponse
import com.example.tugasakhirmobel.data.remote.model.BarangRequest
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BarangRepository @Inject constructor() {

    private val apiService = RetrofitClient.instance.create(BarangApiService::class.java)

    suspend fun fetchSemuaBarang(): Response<BarangListResponse> = apiService.fetchSemuaBarang()

    suspend fun tambahBarang(
        nama: MultipartBody.Part,
        sku: MultipartBody.Part,
        kategori: MultipartBody.Part,
        harga: MultipartBody.Part,
        supplier: MultipartBody.Part,
        stok: MultipartBody.Part,
        stokMinimum: MultipartBody.Part,
        fileGambar: MultipartBody.Part
    ): Result<String> {
        return try {
            val response = apiService.createBarang(
                nama,
                sku,
                kategori,
                harga,
                supplier,
                stok,
                stokMinimum,
                fileGambar
            )
            if (response.isSuccessful) Result.success("Sukses")
            else Result.failure(Exception("Gagal: ${response.code()}"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateBarang(id: Int, request: BarangRequest): Response<Map<String, Any>> =
        apiService.updateBarang(id, request)

    suspend fun hapusBarang(id: Int): Response<Map<String, Any>> = apiService.deleteBarang(id)
}
