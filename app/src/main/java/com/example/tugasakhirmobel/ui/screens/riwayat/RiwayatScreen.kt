package com.example.tugasakhirmobel.ui.screens.riwayat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatScreen() {
    // State untuk Pencarian dan Filter
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }

    // Warna Tema Aplikasi
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF6A1B9A), Color(0xFFE91E63)) // Biru ke Merah sesuai gambar
    )
    val colorMasuk = Color(0xFF0D47A1) // Biru
    val colorKeluar = Color(0xFFD32F2F) // Merah

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {
        // --- 1. HEADER SECTION (GRADASI & STATISTIK) ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradientBackground)
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Column {
                // Judul
                Text("Riwayat Barang", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Pergerakan fisik masuk & keluar", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)

                Spacer(modifier = Modifier.height(20.dp))

                // Kartu Statistik Masuk & Keluar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatBox(modifier = Modifier.weight(1f), icon = Icons.Default.NorthEast, count = "4", label = "Stok Masuk")
                    StatBox(modifier = Modifier.weight(1f), icon = Icons.Default.SouthWest, count = "4", label = "Stok Keluar")
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Kotak Pencarian
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari barang atau operator...", color = Color.White.copy(alpha = 0.6f)) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Cari", tint = Color.White.copy(alpha = 0.7f)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.15f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.15f),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    singleLine = true
                )
            }
        }

        // --- 2. FILTER SECTION (SEMUA, MASUK, KELUAR) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilterTab(text = "Semua", isSelected = selectedFilter == "Semua", onClick = { selectedFilter = "Semua" }, isGradient = true)
            FilterTab(text = "Masuk", isSelected = selectedFilter == "Masuk", onClick = { selectedFilter = "Masuk" })
            FilterTab(text = "Keluar", isSelected = selectedFilter == "Keluar", onClick = { selectedFilter = "Keluar" })
        }

        // --- 3. LIST RIWAYAT BARANG ---
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 100.dp) // Jarak aman untuk Navbar
        ) {
            // Data Dummy Sementara untuk Tampilan Visual
            item {
                HistoryItemCard(
                    title = "Laptop Asus VivoBook 15",
                    description = "Restock dari supplier",
                    date = "09/06/2026 09:15",
                    operator = "Admin Budi",
                    amount = "+5",
                    isMasuk = true,
                    colorMasuk = colorMasuk,
                    colorKeluar = colorKeluar
                )
            }
            item {
                HistoryItemCard(
                    title = "Mouse Wireless Logitech",
                    description = "Diambil divisi Marketing",
                    date = "08/06/2026 14:30",
                    operator = "Admin Sari",
                    amount = "-3",
                    isMasuk = false,
                    colorMasuk = colorMasuk,
                    colorKeluar = colorKeluar
                )
            }
            item {
                HistoryItemCard(
                    title = "Pulpen Pilot G2 (12pcs)",
                    description = "Pembelian rutin",
                    date = "07/06/2026 10:00",
                    operator = "Admin Budi",
                    amount = "+20",
                    isMasuk = true,
                    colorMasuk = colorMasuk,
                    colorKeluar = colorKeluar
                )
            }
        }
    }
}

// --- KOMPONEN PELENGKAP UI ---

@Composable
fun StatBox(modifier: Modifier = Modifier, icon: ImageVector, count: String, label: String) {
    Box(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = Color.White)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(count, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(label, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun FilterTab(text: String, isSelected: Boolean, onClick: () -> Unit, isGradient: Boolean = false) {
    val gradientBrush = Brush.horizontalGradient(listOf(Color(0xFF6A1B9A), Color(0xFFC62828)))

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .then(
                if (isSelected && isGradient) Modifier.background(gradientBrush)
                else if (isSelected) Modifier.background(Color.White).border(1.dp, Color.LightGray, RoundedCornerShape(20.dp))
                else Modifier.background(Color.White).border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(20.dp))
            )
            .padding(horizontal = 20.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected && isGradient) Color.White else if (isSelected) Color.Black else Color.Gray,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun HistoryItemCard(
    title: String,
    description: String,
    date: String,
    operator: String,
    amount: String,
    isMasuk: Boolean,
    colorMasuk: Color,
    colorKeluar: Color
) {
    val statusColor = if (isMasuk) colorMasuk else colorKeluar
    val icon = if (isMasuk) Icons.Default.NorthEast else Icons.Default.SouthWest

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gambar Item dengan Badge Status Overlay
            Box(modifier = Modifier.size(56.dp)) {
                // Placeholder Gambar (Bisa diganti dengan Coil Image nanti)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFE0E0E0), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(title.first().toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                }

                // Overlay Badge Kecil (+ / -) di pojok kanan bawah
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(statusColor, CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                        .align(Alignment.BottomEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Detail Teks Tengah
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1A1A1A))
                Text(description, fontSize = 13.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(date, fontSize = 11.sp, color = Color.Gray)
                    Text(" • ", fontSize = 11.sp, color = Color.Gray)
                    Text(operator, fontSize = 11.sp, color = Color.Gray)
                }
            }

            // Jumlah (Kanan)
            Column(horizontalAlignment = Alignment.End) {
                Text(amount, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = statusColor)
                Text("unit", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}