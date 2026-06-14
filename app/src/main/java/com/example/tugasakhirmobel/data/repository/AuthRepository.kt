package com.example.tugasakhirmobel.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val userRepository: UserRepository
) {
    private val auth = FirebaseAuth.getInstance()

    suspend fun doLogin(email: String, sandi: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, sandi).await()
            val user = result.user

            if (user != null) {
                Result.success(user.uid)
            } else {
                Result.failure(Exception("Pengguna tidak ditemukan"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Fungsi baru untuk verifikasi status aktif di database PostgreSQL
    // Fungsi untuk mengambil data user aktif dari backend
    suspend fun checkUserActiveStatus() = userRepository.getCurrentUser()
}
