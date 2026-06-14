package com.example.tugasakhirmobel.ui.screens.log

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Shield
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
fun LogScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }

    val gradientHeader = Brush.verticalGradient(
        colors = listOf(Color(0xFF6A1B9A), Color(0xFFC62828))
    )

    // 👇 1. UBAH COLUMN TERLUAR MENJADI LAZYCOLUMN 👇
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA)),
        contentPadding = PaddingValues(bottom = 100.dp) // Jarak aman navbar
    ) {

        // 👇 2. BUNGKUS HEADER DENGAN item { } 👇
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(gradientHeader)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Shield, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Log Admin", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Riwayat semua perubahan oleh administrator", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(20.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari produk, akun, atau admin...", color = Color.White.copy(alpha = 0.6f)) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Cari", tint = Color.White.copy(alpha = 0.7f)) },
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
        }

        // 👇 3. BUNGKUS FILTER DENGAN item { } 👇
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilterTab(text = "Semua", isSelected = selectedFilter == "Semua", onClick = { selectedFilter = "Semua" }, isGradient = true)
                FilterTab(text = "Tambah Barang", isSelected = selectedFilter == "Tambah Barang", onClick = { selectedFilter = "Tambah Barang" })
                FilterTab(text = "Edit Barang", isSelected = selectedFilter == "Edit Barang", onClick = { selectedFilter = "Edit Barang" })
                FilterTab(text = "Hapus Barang", isSelected = selectedFilter == "Hapus Barang", onClick = { selectedFilter = "Hapus Barang" })
            }
        }

        // 👇 4. LIST ITEM LANGSUNG DITULIS (Tidak perlu LazyColumn lagi karena sudah di dalam) 👇
        item { DateHeader("09/06/2026") }
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                LogItemCard(
                    actionType = "Tambah Barang",
                    productName = "Laptop Asus VivoBook 15",
                    description = "Produk baru ditambahkan ke sistem",
                    operator = "Admin Budi",
                    time = "09:00",
                    icon = Icons.Default.Add,
                    tintColor = Color(0xFF10B981)
                )
            }
        }
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                LogItemCard(
                    actionType = "Stok Masuk",
                    productName = "Laptop Asus VivoBook 15",
                    description = "+5 unit dari supplier PT Asus Indonesia",
                    operator = "Admin Budi",
                    time = "09:15",
                    icon = Icons.Default.NorthEast,
                    tintColor = Color(0xFF3B82F6)
                )
            }
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }
        item { DateHeader("08/06/2026") }
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                LogItemCard(
                    actionType = "Edit Barang",
                    productName = "Mouse Wireless Logitech",
                    description = "Mengubah batas stok minimum",
                    operator = "Admin Sari",
                    time = "14:30",
                    icon = Icons.Default.Edit,
                    tintColor = Color(0xFF8B5CF6)
                )
            }
        }
    }
}

// --- KOMPONEN PELENGKAP UI ---

@Composable
fun DateHeader(date: String) {
    Text(
        text = date,
        fontSize = 13.sp,
        color = Color.Gray,
        modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 4.dp)
    )
}

@Composable
fun FilterTab(text: String, isSelected: Boolean, onClick: () -> Unit, isGradient: Boolean = false) {
    val gradientBrush = Brush.horizontalGradient(listOf(Color(0xFF3F1A9C), Color(0xFFC62828)))

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
            fontSize = 13.sp
        )
    }
}

@Composable
fun LogItemCard(
    actionType: String,
    productName: String,
    description: String,
    operator: String,
    time: String,
    icon: ImageVector,
    tintColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Ikon Kiri (Lingkaran dengan warna dinamis)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(tintColor.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = tintColor, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Konten Kanan
            Column(modifier = Modifier.weight(1f)) {
                // Badge Status
                Surface(
                    color = tintColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = actionType,
                        color = tintColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Detail Produk & Deskripsi
                Text(text = productName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1A1A1A))
                Text(text = description, fontSize = 13.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(10.dp))

                // Baris Info Bawah (Admin & Waktu)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Shield, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = operator, fontSize = 12.sp, color = Color.Gray)
                    Text(text = "  •  ", fontSize = 12.sp, color = Color.LightGray)
                    Text(text = time, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}