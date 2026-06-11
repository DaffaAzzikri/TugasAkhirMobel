package com.example.tugasakhirmobel.data.remote.interceptor

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Interceptor
import okhttp3.Response

class FirebaseAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        val user = FirebaseAuth.getInstance().currentUser

        // Jika user sedang login, ambil tokennya
        if (user != null) {
            try {
                // Interceptor milik Retrofit berjalan di background thread,
                // sehingga kita BISA menggunakan Tasks.await() dengan aman untuk nge-block
                // eksekusi sampai token Firebase berhasil didapatkan.
                val task = user.getIdToken(false)
                val tokenResult = Tasks.await(task)
                val token = tokenResult.token

                // Selipkan token ke dalam Header HTTP
                if (token != null) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }
            } catch (e: Exception) {
                // Akan masuk ke sini jika gagal ambil token (misal: internet putus)
                e.printStackTrace()
            }
        }

        // Lanjutkan perjalanan request ke server FastAPI
        return chain.proceed(requestBuilder.build())
    }
}