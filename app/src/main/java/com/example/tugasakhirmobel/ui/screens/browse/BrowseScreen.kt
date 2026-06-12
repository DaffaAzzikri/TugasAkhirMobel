package com.example.tugasakhirmobel.ui.screens.browse

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(
    onAddClick: () -> Unit
) {
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF3F1A9C), Color(0xFFC62828))
    )

    // State untuk mengingat filter mana yang sedang aktif/diklik
    var selectedFilter by remember { mutableStateOf("Semua Status") }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F7FA))
    ) {
        // --- 1. HEADER DENGAN SEARCH BAR MENGAMBANG ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradientBackground)
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Daftar Barang", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("12 dari 12 produk", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    }

                    // Tombol Tambah Produk Bergaya Floating Mini
                    Button(
                        onClick = onAddClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah", tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Tambah", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Kolom Cari Nama atau SKU
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Cari nama atau SKU...", color = Color.White.copy(alpha = 0.6f)) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Cari", tint = Color.White.copy(alpha = 0.7f)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.12f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.12f),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.White
                    )
                )
            }
        }

        // --- 2. HORIZONTAL FILTER BADGES (BISA DI-CLICK & DI-SCROLL) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedFilter == "Kategori",
                onClick = { selectedFilter = "Kategori" },
                label = { Text("Kategori ▾") }
            )

            FilterChip(
                selected = selectedFilter == "Semua Status",
                onClick = { selectedFilter = "Semua Status" },
                label = { Text("Semua Status") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFC62828),
                    selectedLabelColor = Color.White
                )
            )

            FilterChip(
                selected = selectedFilter == "Tersedia",
                onClick = { selectedFilter = "Tersedia" },
                label = { Text("Tersedia") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF2E7D32),
                    selectedLabelColor = Color.White
                )
            )

            FilterChip(
                selected = selectedFilter == "Stok Rendah",
                onClick = { selectedFilter = "Stok Rendah" },
                label = { Text("Stok Rendah") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFFFB300),
                    selectedLabelColor = Color.White
                )
            )
        }

        // --- 3. LIST OF ITEMS (PADDINGS BERSIH & SCROLLABLE) ---
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp) // Jarak bawah 100.dp aman dari navbar
        ) {
            item { ProductItemCard(name = "Laptop Asus VivoBook 15", code = "ELC-001 · PT Asus Indonesia", category = "Elektronik", stock = "12 / min 5", price = "Rp 7.500.000", status = "Tersedia", isAvailable = true) }
            item { ProductItemCard(name = "Mouse Wireless Logitech", code = "ELC-002 · PT Logitech Indo", category = "Elektronik", stock = "45 / min 10", price = "Rp 175.000", status = "Tersedia", isAvailable = true) }
            item { ProductItemCard(name = "Keyboard Mechanical Redragon", code = "ELC-003 · Redragon Store", category = "Elektronik", stock = "3 / min 5", price = "Rp 650.000", status = "Stok Rendah", isAvailable = false) }
        }
    }
}

@Composable
fun ProductItemCard(name: String, code: String, category: String, stock: String, price: String, status: String, isAvailable: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Kotak Foto / Placeholder Gambar Produk
                    Box(
                        modifier = Modifier.size(54.dp).background(Color(0xFFEEEEEE), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Inventory, contentDescription = null, tint = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1A1A1A))
                        Text(code, fontSize = 11.sp, color = Color.Gray)
                    }
                }

                // Status Badge
                Card(
                    colors = CardDefaults.cardColors(containerColor = if (isAvailable) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(status, color = if (isAvailable) Color(0xFF2E7D32) else Color(0xFFE65100), fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Baris Informasi Kategori dan Stok
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6)), shape = RoundedCornerShape(6.dp)) {
                    Text(category, color = Color(0xFF3F1A9C), fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                }
                Text("Stok: ", color = Color.Gray, fontSize = 12.sp)
                Text(stock, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))

            // Baris Harga dan Tombol Aksi Cepat Bawah
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(price, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val btnModifier = Modifier.size(32.dp).background(Color(0xFFF5F7FA), CircleShape)
                    IconButton(onClick = {}, modifier = btnModifier) { Icon(Icons.Default.ArrowOutward, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    IconButton(onClick = {}, modifier = btnModifier) { Icon(Icons.Default.ArrowDownward, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    IconButton(onClick = {}, modifier = btnModifier) { Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp)) }
                    IconButton(onClick = {}, modifier = btnModifier) { Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Red) }
                }
            }
        }
    }
}