package com.example.tugasakhirmobel.data.remote.api

import com.example.tugasakhirmobel.data.remote.model.BarangListResponse
import com.example.tugasakhirmobel.data.remote.model.BarangRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface BarangApiService {

    @GET("api/v1/barang")
    suspend fun fetchSemuaBarang(): Response<BarangListResponse>

    @Multipart
    @POST("api/v1/barang")
    suspend fun createBarang(
        @Part nama_barang: MultipartBody.Part,
        @Part sku: MultipartBody.Part,
        @Part kategori: MultipartBody.Part,
        @Part harga: MultipartBody.Part,
        @Part supplier: MultipartBody.Part,
        @Part stok: MultipartBody.Part,
        @Part stok_minimum: MultipartBody.Part,
        @Part file_gambar: MultipartBody.Part
    ): Response<Map<String, Any>>

    @PUT("api/v1/barang/{barang_id}")
    suspend fun updateBarang(
        @Path("barang_id") id: Int,
        @Body request: BarangRequest
    ): Response<Map<String, Any>>

    @DELETE("api/v1/barang/{barang_id}")
    suspend fun deleteBarang(@Path("barang_id") id: Int): Response<Map<String, Any>>
}
