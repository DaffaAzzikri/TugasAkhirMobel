package com.example.tugasakhirmobel.ui.screens.browse

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tugasakhirmobel.data.remote.model.BarangModel
import com.example.tugasakhirmobel.ui.screens.barang.BarangViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(
    onAddClick: () -> Unit,
    onEditClick: (BarangModel) -> Unit,
    viewModel: BarangViewModel
) {
    val barangList by viewModel.barangList.collectAsState()

    // State untuk Dialog Delete
    var showDeleteDialog by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<BarangModel?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadBarang()
    }

    if (showDeleteDialog && itemToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Barang?", fontWeight = FontWeight.Bold) },
            text = { Text("Apakah Anda yakin ingin menghapus '${itemToDelete?.namaBarang}'? Tindakan ini tidak dapat dibatalkan.") },
            confirmButton = {
                TextButton(onClick = {
                    itemToDelete?.id?.let { viewModel.hapusBarang(it) }
                    showDeleteDialog = false
                }) { Text("Hapus", color = Color.Red, fontWeight = FontWeight.Bold) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Batal") }
            }
        )
    }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF3F1A9C), Color(0xFFE91E63))
    )

    // State untuk mengingat filter tab yang aktif
    var selectedFilter by remember { mutableStateOf("Semua Status") }

    // State khusus untuk Dropdown Kategori
    var kategoriExpanded by remember { mutableStateOf(false) }
    var selectedKategori by remember { mutableStateOf("Semua Kategori") }
    val daftarKategori = listOf("Semua Kategori", "Elektronik", "Furnitur", "ATK", "Pakaian", "Makanan & Minuman", "Peralatan")

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
                        Text("${barangList.size} produk", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    }

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

        // --- 2. HORIZONTAL FILTER BADGES (DENGAN DROPDOWN KATEGORI) ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box {
                FilterChip(
                    selected = selectedKategori != "Semua Kategori",
                    onClick = { kategoriExpanded = true },
                    label = {
                        Text(if (selectedKategori == "Semua Kategori") "Kategori ▾" else "$selectedKategori ▾")
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFE3F2FD),
                        selectedLabelColor = Color(0xFF0D47A1)
                    )
                )

                DropdownMenu(
                    expanded = kategoriExpanded,
                    onDismissRequest = { kategoriExpanded = false },
                    modifier = Modifier
                        .background(Color.White)
                        .width(240.dp)
                ) {
                    daftarKategori.forEach { kategori ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = kategori,
                                        color = Color.Black,
                                        fontWeight = if (selectedKategori == kategori) FontWeight.Bold else FontWeight.Normal
                                    )
                                    if (selectedKategori == kategori) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Terpilih",
                                            tint = Color(0xFF0D47A1),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            onClick = {
                                selectedKategori = kategori
                                kategoriExpanded = false
                            }
                        )
                    }
                }
            }

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

        // --- 3. LIST OF ITEMS ---
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp)
        ) {
            items(barangList) { barang ->
                ProductItemCard(
                    name = barang.namaBarang,
                    code = "${barang.sku} · ${barang.supplier}",
                    category = barang.kategori,
                    stock = "${barang.stok}",
                    price = "Rp ${barang.harga}",
                    status = if (barang.stok > 0) "Tersedia" else "Habis",
                    isAvailable = barang.stok > 0,
                    imageUrl = barang.imageUrl,
                    onEdit = { onEditClick(barang) },
                    onDelete = {
                        itemToDelete = barang
                        showDeleteDialog = true
                    }
                )
            }
        }
    }
}

@Composable
fun ProductItemCard(
    name: String,
    code: String,
    category: String,
    stock: String,
    price: String,
    status: String,
    isAvailable: Boolean,
    imageUrl: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    // 1. Siapkan Painter dari ImageVector satu kali saja
    val placeholderPainter = rememberVectorPainter(Icons.Default.Image)
    val errorPainter = rememberVectorPainter(Icons.Default.Inventory)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // --- BAGIAN GAMBAR ---
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .background(Color(0xFFEEEEEE), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        coil.compose.AsyncImage(
                            // 2. Gunakan ImageRequest untuk kontrol lebih detail
                            model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                                .data(imageUrl.ifBlank { null }) // Berikan null jika string kosong
                                .crossfade(true)
                                .build(),
                            contentDescription = "Foto $name",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop,
                            // 3. Masukkan Painter ke parameter yang tepat
                            placeholder = placeholderPainter,
                            error = errorPainter
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF1A1A1A))
                        Text(code, fontSize = 11.sp, color = Color.Gray)
                    }
                }

                // Status Badge
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isAvailable) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        status,
                        color = if (isAvailable) Color(0xFF2E7D32) else Color(0xFFE65100),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Detail Kategori & Stok
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6)),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        category,
                        color = Color(0xFF3F1A9C),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Text("Stok: ", color = Color.Gray, fontSize = 12.sp)
                Text(stock, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))

            // Harga & Tombol Aksi
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(price, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val btnModifier = Modifier.size(32.dp).background(Color(0xFFF5F7FA), CircleShape)
                    IconButton(onClick = onEdit, modifier = btnModifier) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(16.dp))
                    }
                    IconButton(onClick = onDelete, modifier = btnModifier) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus", modifier = Modifier.size(16.dp), tint = Color.Red)
                    }
                }
            }
        }
    }
}
