// C:/Users/ACER/TugasAkhirMobel/app/src/main/java/com/example/tugasakhirmobel/ui/screens/barang/BarangViewModel.kt

package com.example.tugasakhirmobel.ui.screens.barang

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugasakhirmobel.data.remote.model.BarangModel
import com.example.tugasakhirmobel.data.remote.model.BarangRequest
import com.example.tugasakhirmobel.data.repository.BarangRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

sealed class BarangState {
    object Idle : BarangState()
    object Loading : BarangState()
    object Success : BarangState()
    data class Error(val message: String) : BarangState()
}

@HiltViewModel
class BarangViewModel @Inject constructor(
    private val repository: BarangRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _barangState = MutableStateFlow<BarangState>(BarangState.Idle)
    val barangState: StateFlow<BarangState> = _barangState.asStateFlow()

    private val _barangList = MutableStateFlow<List<BarangModel>>(emptyList())
    val barangList: StateFlow<List<BarangModel>> = _barangList.asStateFlow()

    // State untuk menyimpan item yang akan diedit
    var itemToEdit: BarangModel? = null

    fun loadBarang() {
        viewModelScope.launch {
            try {
                val response = repository.fetchSemuaBarang()
                if (response.isSuccessful) {
                    _barangList.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun tambahBarangBaru(
        nama: String, sku: String, kategori: String, harga: String,
        supplier: String, stokTeks: String, imageUri: Uri?
    ) {
        if (nama.isBlank() || sku.isBlank() || kategori.isBlank() || stokTeks.isBlank() || imageUri == null) {
            _barangState.value = BarangState.Error("Kolom bertanda * wajib diisi!")
            return
        }

        _barangState.value = BarangState.Loading
        viewModelScope.launch {
            try {
                val file = uriToFile(imageUri, context) ?: throw Exception("Gagal memproses gambar")
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

                val bodyNama = MultipartBody.Part.createFormData("nama_barang", nama)
                val bodySku = MultipartBody.Part.createFormData("sku", sku)
                val bodyKategori = MultipartBody.Part.createFormData("kategori", kategori)
                val bodyHarga = MultipartBody.Part.createFormData("harga", if(harga.isBlank()) "0" else harga)
                val bodySupplier = MultipartBody.Part.createFormData("supplier", supplier)
                val bodyStok = MultipartBody.Part.createFormData("stok", stokTeks)
                val bodyGambar = MultipartBody.Part.createFormData("file_gambar", file.name, requestFile)

                val hasil = repository.tambahBarang(bodyNama, bodySku, bodyKategori, bodyHarga, bodySupplier, bodyStok, bodyGambar)

                hasil.fold(
                    onSuccess = {
                        _barangState.value = BarangState.Success
                        loadBarang()
                    },
                    onFailure = { _barangState.value = BarangState.Error("Gagal menyimpan data") }
                )
            } catch (e: Exception) {
                _barangState.value = BarangState.Error(e.localizedMessage ?: "Gagal")
            }
        }
    }

    fun updateBarang(
        id: Int, nama: String, sku: String, kategori: String, harga: String,
        supplier: String, stok: String, imageUrl: String
    ) {
        _barangState.value = BarangState.Loading
        viewModelScope.launch {
            try {
                val request = BarangRequest(
                    namaBarang = nama,
                    sku = sku,
                    kategori = kategori,
                    harga = harga.toIntOrNull() ?: 0,
                    supplier = supplier,
                    stok = stok.toIntOrNull() ?: 0,
                    imageUrl = imageUrl
                )
                val response = repository.updateBarang(id, request)
                if (response.isSuccessful) {
                    _barangState.value = BarangState.Success
                    loadBarang()
                } else {
                    _barangState.value = BarangState.Error("Gagal memperbarui data")
                }
            } catch (e: Exception) {
                _barangState.value = BarangState.Error(e.localizedMessage ?: "Terjadi kesalahan")
            }
        }
    }

    fun hapusBarang(id: Int) {
        _barangState.value = BarangState.Loading
        viewModelScope.launch {
            try {
                val response = repository.hapusBarang(id)
                if (response.isSuccessful) {
                    _barangState.value = BarangState.Success
                    loadBarang()
                } else {
                    _barangState.value = BarangState.Error("Gagal menghapus barang")
                }
            } catch (e: Exception) {
                _barangState.value = BarangState.Error(e.localizedMessage ?: "Error")
            }
        }
    }

    private fun uriToFile(uri: Uri, context: Context): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File(context.cacheDir, "temp_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            file
        } catch (e: Exception) { null }
    }

    fun resetState() {
        _barangState.value = BarangState.Idle
        itemToEdit = null
    }
}