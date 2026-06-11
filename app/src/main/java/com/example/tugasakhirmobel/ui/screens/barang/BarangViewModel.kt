package com.example.tugasakhirmobel.ui.screens.barang

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhirmobel.data.repository.BarangRepository // Sesuaikan import
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Pembungkus status layar khusus untuk fitur Barang
sealed class BarangState {
    object Idle : BarangState()
    object Loading : BarangState()
    object Success : BarangState()
    data class Error(val message: String) : BarangState()
}

@HiltViewModel // Beritahu Hilt untuk menyuntikkan dependensi ke ViewModel ini
class BarangViewModel @Inject constructor(
    private val repository: BarangRepository // Sang Koki otomatis disuntikkan oleh Hilt
) : ViewModel() {

    // Menyimpan status untuk dibaca oleh UI buatan Rizqan
    private val _barangState = MutableStateFlow<BarangState>(BarangState.Idle)
    val barangState: StateFlow<BarangState> = _barangState.asStateFlow()

    /**
     * Fungsi yang akan dipanggil oleh tombol "Simpan" di UI.
     * Stok sengaja diterima sebagai String karena input dari TextField Compose adalah String.
     */
    fun tambahBarangBaru(nama: String, stokTeks: String, imageUri: Uri?) {
        // 1. Validasi Input Kosong
        if (nama.isBlank() || stokTeks.isBlank() || imageUri == null) {
            _barangState.value = BarangState.Error("Semua kolom dan foto wajib diisi!")
            return
        }

        // 2. Validasi Tipe Data Stok (Harus Angka)
        val stok = stokTeks.toIntOrNull()
        if (stok == null || stok < 0) {
            _barangState.value = BarangState.Error("Stok harus berupa angka bulat dan tidak boleh minus!")
            return
        }

        // 3. Ubah status menjadi Loading (UI bisa memunculkan indikator berputar)
        _barangState.value = BarangState.Loading

        // 4. Perintahkan Repositori bekerja di background thread
        viewModelScope.launch {
            val hasil = repository.tambahBarang(nama, stok, imageUri)

            hasil.fold(
                onSuccess = { pesanSukses ->
                    _barangState.value = BarangState.Success
                    println("✅ Sukses: $pesanSukses")
                },
                onFailure = { error ->
                    val pesanError = error.localizedMessage ?: "Terjadi kesalahan saat menyimpan data"
                    _barangState.value = BarangState.Error(pesanError)
                }
            )
        }
    }

    // Fungsi untuk mengembalikan layar ke posisi normal (misalnya setelah notifikasi sukses ditutup)
    fun resetState() {
        _barangState.value = BarangState.Idle
    }
}