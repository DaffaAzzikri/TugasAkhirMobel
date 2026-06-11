package com.example.tugasakhirmobel

import android.app.Application
import com.cloudinary.android.MediaManager
import dagger.hilt.android.HiltAndroidApp // <-- Tambahkan import ini

@HiltAndroidApp // <-- ANOTASI INI SANGAT WAJIB!
class InventarisApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val config = mapOf("cloud_name" to "dau9gb3pp")
        MediaManager.init(this, config)
    }
}
