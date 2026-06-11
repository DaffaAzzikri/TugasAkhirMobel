package com.example.tugasakhirmobel.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen(
    onLogoutClick: () -> Unit,
    onPingClick: () -> Unit,
    onAddProductClick: () -> Unit,
    onBrowseClick: () -> Unit // 1. TAMBAH PARAMETER UNTUK BROWSE
) {
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF3F1A9C), Color(0xFFC62828))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {
        // --- HEADER GRADASI ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradientBackground)
                .padding(top = 24.dp, start = 24.dp, end = 24.dp, bottom = 40.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Selamat datang,", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                        Text("Super Admin", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                    IconButton(
                        onClick = onLogoutClick,
                        modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Total Nilai Inventaris", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                        Text("Rp 133.763.000", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).background(Color(0xFFFFD600), CircleShape))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("12 produk terdaftar", color = Color.White.copy(alpha = 0.9f), fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // --- KARTU STATISTIK MELAYANG ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y = (-20).dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatCard(modifier = Modifier.weight(1f), title = "Tersedia", count = "7", icon = Icons.Default.Inventory, iconColor = Color(0xFF0D47A1))
            StatCard(modifier = Modifier.weight(1f), title = "Stok Rendah", count = "3", icon = Icons.Default.Warning, iconColor = Color(0xFFE65100))
            StatCard(modifier = Modifier.weight(1f), title = "Habis", count = "2", icon = Icons.Default.NewReleases, iconColor = Color(0xFFB71C1C))
        }

        // --- DAFTAR PERLU PERHATIAN & UTILITY BUTTONS ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onAddProductClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F1A9C)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Tambah Produk", fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = onPingClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Test API")
                }
            }

            // 2. MODIFIKASI: Mengubah Text menjadi Row yang berisi tombol "Lihat Semua"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Perlu Perhatian", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                TextButton(onClick = onBrowseClick) {
                    Text("Lihat Semua", color = Color(0xFF3F1A9C), fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item { AlertItem(name = "Keyboard Mechanical Redragon", info = "Stok: 3 / Min: 5", tag = "Stok Rendah", isCritical = false) }
                item { AlertItem(name = "Monitor LG 24 Inch IPS", info = "Stok: 0 / Min: 3", tag = "Habis", isCritical = true) }
                item { AlertItem(name = "Kursi Ergonomis Highback", info = "Stok: 5 / Min: 5", tag = "Stok Rendah", isCritical = false) }
            }
        }
    }
}

// Komponen StatCard dan AlertItem tetap sama seperti sebelumnya...
@Composable
fun StatCard(modifier: Modifier = Modifier, title: String, count: String, icon: androidx.compose.ui.graphics.vector.ImageVector, iconColor: Color) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Icon(imageVector = icon, contentDescription = title, tint = iconColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(count, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
            Text(title, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun AlertItem(name: String, info: String, tag: String, isCritical: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).background(if (isCritical) Color.Red else Color(0xFFFFB300), CircleShape))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(name, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Text(info, color = Color.Gray, fontSize = 12.sp)
                }
            }
            Card(
                colors = CardDefaults.cardColors(containerColor = if (isCritical) Color(0xFFFFEBEE) else Color(0xFFFFF8E1)),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(text = tag, color = if (isCritical) Color.Red else Color(0xFFFFB300), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
            }
        }
    }
}