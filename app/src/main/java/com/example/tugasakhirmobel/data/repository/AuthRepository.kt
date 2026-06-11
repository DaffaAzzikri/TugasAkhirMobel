package com.example.tugasakhirmobel.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    // Fungsi ini dipanggil oleh ViewModel
    suspend fun doLogin(email: String, sandi: String): Result<String> {
        return try {
            // Tembak ke Firebase dan tunggu hasilnya (await)
            val result = auth.signInWithEmailAndPassword(email, sandi).await()
            val user = result.user

            if (user != null) {
                Result.success(user.uid) // Berhasil, kembalikan UID
            } else {
                Result.failure(Exception("Pengguna tidak ditemukan"))
            }
        } catch (e: Exception) {
            // Gagal (misal: sandi salah atau internet mati)
            Result.failure(e)
        }
    }
}