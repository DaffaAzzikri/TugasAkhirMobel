package com.example.tugasakhirmobel.data.remote.media

import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object CloudinaryHelper {

    /**
     * Mengunggah gambar ke Cloudinary dan mengembalikan URL publiknya.
     * Menggunakan suspend function agar tidak membekukan antarmuka (UI).
     */
    suspend fun uploadImage(fileUri: Uri): String = suspendCancellableCoroutine { continuation ->

        // Memulai proses upload ke mesin Cloudinary
        MediaManager.get().upload(fileUri)
            // GANTI INI: Gunakan nama 'Upload Preset' Anda yang diatur di setting Cloudinary (Unsigned)
            .unsigned("inventaris_mobin")
            .callback(object : UploadCallback {

                override fun onStart(requestId: String) {
                    println("☁️ Cloudinary: Mulai mengunggah...")
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    // Bisa dihubungkan ke UI Loading Bar jika mau
                    val progress = (bytes.toDouble() / totalBytes) * 100
                    println("☁️ Cloudinary: Progress ${progress.toInt()}%")
                }

                override fun onSuccess(requestId: String, resultData: Map<*, *>) {
                    // Berhasil! Tangkap URL aman (HTTPS) dari Cloudinary
                    val secureUrl = resultData["secure_url"] as? String ?: ""
                    println("☁️ Cloudinary: Sukses! URL -> $secureUrl")

                    // Kembalikan URL ini ke ViewModel
                    continuation.resume(secureUrl)
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    println("❌ Cloudinary Error: ${error.description}")
                    continuation.resumeWithException(Exception(error.description))
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    continuation.resumeWithException(Exception("Upload tertunda: ${error.description}"))
                }
            })
            .dispatch()
    }
}