package com.example.tugasakhirmobel.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tugasakhirmobel.data.local.dao.BarangDao
import com.example.tugasakhirmobel.data.local.entity.BarangEntity

@Database(
    entities = [BarangEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun barangDao(): BarangDao
}
