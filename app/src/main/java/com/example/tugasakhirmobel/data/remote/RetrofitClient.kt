package com.example.tugasakhirmobel.data.remote

import com.example.tugasakhirmobel.data.remote.interceptor.FirebaseAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // CATATAN KRUSIAL:
    // Jika Anda menggunakan Emulator Android Studio dan FastAPI berjalan di VSCode (localhost),
    // Anda TIDAK BISA menggunakan 127.0.0.1.
    // Emulator Android mengenali mesin komputer Anda dengan IP khusus yaitu 10.0.2.2
    private const val BASE_URL = "http://10.0.2.2:8000/"
    // (Jika nanti pakai HP asli dengan kabel data, ganti dengan IP Address WiFi laptop Anda, misal: 192.168.1.5)

    // 1. Buat mesin pencatat log (agar kita bisa melihat error API di tab Logcat Android Studio)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // 2. Pasang FirebaseAuthInterceptor yang baru saja Anda buat
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(FirebaseAuthInterceptor()) // <-- Di sini agen rahasianya dipasang!
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    // 3. Bangun Retrofit
    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // Pengurai JSON
            .build()
    }
}