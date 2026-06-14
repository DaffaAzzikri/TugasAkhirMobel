package com.example.tugasakhirmobel.data.local.dao

import androidx.room.*
import com.example.tugasakhirmobel.data.local.entity.BarangEntity

@Dao
interface BarangDao {
    @Query("SELECT * FROM barang")
    suspend fun getAllBarang(): List<BarangEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(barang: List<BarangEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(barang: BarangEntity)

    @Update
    suspend fun update(barang: BarangEntity)

    @Delete
    suspend fun delete(barang: BarangEntity)

    @Query("DELETE FROM barang")
    suspend fun clearAll()
}
