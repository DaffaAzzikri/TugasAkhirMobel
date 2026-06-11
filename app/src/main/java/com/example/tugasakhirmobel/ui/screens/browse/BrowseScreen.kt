package com.example.tugasakhirmobel.ui.screens.browse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 1. Data Class sederhana untuk menampung data palsu
data class BarangDummy(
    val id: Int,
    val nama: String,
    val stok: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen() {
    // 2. Daftar data palsu (Dummy Data)
    val daftarBarang = listOf(
        BarangDummy(1, "Susu Ultra Milk 1L", 24),
        BarangDummy(2, "Indomie Goreng (Karton)", 150),
        BarangDummy(3, "Beras Maknyuss 5kg", 8), // Stok tipis untuk melihat perbedaan warna
        BarangDummy(4, "Kopi Kapal Api", 45),
        BarangDummy(5, "Minyak Goreng Bimoli 2L", 3)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Barang", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        // 3. LazyColumn untuk membuat daftar yang bisa di-scroll
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp) // Jarak antar kartu barang
        ) {
            items(daftarBarang) { barang ->
                ItemBarangCard(barang = barang)
            }
        }
    }
}

// 4. Komponen desain untuk masing-masing kartu barang
@Composable
fun ItemBarangCard(barang: BarangDummy) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder Foto Barang (menggunakan kotak dan ikon sementara)
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Foto ${barang.nama}",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Teks Nama Barang dan Sisa Stok
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = barang.nama,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Logika UI: Jika stok di bawah 10, teks berubah jadi merah
                val warnaStok = if (barang.stok < 10) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                Text(
                    text = "Sisa stok: ${barang.stok}",
                    fontSize = 14.sp,
                    color = warnaStok,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}