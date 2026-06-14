package com.example.tugasakhirmobel.ui.screens.browse

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
    
    // State untuk Dialog Stok
    var showStokDialog by remember { mutableStateOf(false) }
    var isStokMasukMode by remember { mutableStateOf(true) }
    var itemForStokTransaksi by remember { mutableStateOf<BarangModel?>(null) }

    // State untuk filter dan search
    var selectedFilter by remember { mutableStateOf("Semua Status") }
    var selectedKategori by remember { mutableStateOf("Semua Kategori") }
    var searchQuery by remember { mutableStateOf("") }
    var kategoriExpanded by remember { mutableStateOf(false) }

    val daftarKategori = listOf("Semua Kategori", "Elektronik", "Furnitur", "ATK", "Pakaian", "Makanan & Minuman", "Peralatan")

    // Logic pencarian dan filter
    val filteredList = remember(barangList, searchQuery, selectedFilter, selectedKategori) {
        barangList.filter { barang ->
            // 1. Pencarian berdasarkan Nama atau SKU
            val matchSearch = barang.namaBarang.contains(searchQuery, ignoreCase = true) || 
                             barang.sku.contains(searchQuery, ignoreCase = true)
            
            // 2. Filter berdasarkan Kategori
            val matchKategori = selectedKategori == "Semua Kategori" || barang.kategori == selectedKategori
            
            // 3. Filter berdasarkan Status Stok
            val matchStatus = when (selectedFilter) {
                "Semua Status" -> true
                "Habis" -> barang.stok <= 0
                "Stok Rendah" -> barang.stok > 0 && barang.stok <= barang.stokMinimum
                "Tersedia" -> barang.stok > barang.stokMinimum
                else -> true
            }

            matchSearch && matchKategori && matchStatus
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadBarang()
    }

    // Dialog Konfirmasi Hapus
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

    // Dialog Transaksi Stok (Masuk/Keluar)
    if (showStokDialog && itemForStokTransaksi != null) {
        val isMasuk = isStokMasukMode
        val title = if (isMasuk) "Stok Masuk" else "Stok Keluar"
        val buttonColor = if (isMasuk) Color(0xFF0D47A1) else Color(0xFFD32F2F)

        var jumlahInput by remember { mutableStateOf("") }
        var keteranganInput by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showStokDialog = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    IconButton(onClick = { showStokDialog = false }, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup")
                    }
                }
            },
            text = {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF5F7FA), RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.LightGray, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            coil.compose.AsyncImage(
                                model = itemForStokTransaksi!!.imageUrl,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(itemForStokTransaksi!!.namaBarang, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Stok saat ini: ${itemForStokTransaksi!!.stok} unit", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Jumlah", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = jumlahInput,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() }) {
                                jumlahInput = input
                            }
                        },
                        placeholder = { Text("Masukkan jumlah") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Keterangan", fontSize = 12.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = keteranganInput,
                        onValueChange = { keteranganInput = it },
                        placeholder = { Text("Opsional") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val jumlahLengkap = jumlahInput.toIntOrNull() ?: 0
                        if (jumlahLengkap > 0) {
                            viewModel.transaksiStok(
                                id = itemForStokTransaksi!!.id,
                                jumlahTransaksi = jumlahLengkap,
                                isMasuk = isMasuk,
                                keterangan = keteranganInput,
                                item = itemForStokTransaksi!!
                            )
                            showStokDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = jumlahInput.isNotBlank()
                ) {
                    Text("Konfirmasi", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF3F1A9C), Color(0xFFE91E63))
    )

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F7FA))
    ) {
        // --- 1. HEADER & SEARCH BAR ---
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
                        Text("${filteredList.size} produk", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
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

                // Implementasi Bar Pencarian
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari nama atau SKU...", color = Color.White.copy(alpha = 0.6f)) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Cari", tint = Color.White.copy(alpha = 0.7f)) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Hapus", tint = Color.White.copy(alpha = 0.7f))
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.12f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.12f),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    singleLine = true
                )
            }
        }

        // --- 2. FILTER BADGES ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Dropdown Kategori
            Box {
                FilterChip(
                    selected = selectedKategori != "Semua Kategori",
                    onClick = { kategoriExpanded = true },
                    label = { Text(if (selectedKategori == "Semua Kategori") "Kategori ▾" else "$selectedKategori ▾") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFE3F2FD),
                        selectedLabelColor = Color(0xFF0D47A1)
                    )
                )

                DropdownMenu(
                    expanded = kategoriExpanded,
                    onDismissRequest = { kategoriExpanded = false },
                    modifier = Modifier.background(Color.White).width(240.dp)
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
                                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color(0xFF0D47A1), modifier = Modifier.size(18.dp))
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

            // Chips Status Stok
            listOf("Semua Status", "Tersedia", "Stok Rendah", "Habis").forEach { status ->
                FilterChip(
                    selected = selectedFilter == status,
                    onClick = { selectedFilter = status },
                    label = { Text(status) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = when(status) {
                            "Tersedia" -> Color(0xFF2E7D32)
                            "Stok Rendah" -> Color(0xFFFFB300)
                            "Habis" -> Color(0xFFC62828)
                            else -> Color(0xFF3F1A9C)
                        },
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // --- 3. DAFTAR BARANG (LazyColumn) ---
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 100.dp)
        ) {
            items(filteredList) { barang ->
                val statusText = when {
                    barang.stok <= 0 -> "Habis"
                    barang.stok <= barang.stokMinimum -> "Stok Rendah"
                    else -> "Tersedia"
                }
                ProductItemCard(
                    name = barang.namaBarang,
                    code = "${barang.sku} · ${barang.supplier}",
                    category = barang.kategori,
                    stock = "${barang.stok}",
                    price = "Rp ${barang.harga}",
                    status = statusText,
                    isAvailable = barang.stok > barang.stokMinimum,
                    imageUrl = barang.imageUrl,
                    onStokMasuk = {
                        itemForStokTransaksi = barang
                        isStokMasukMode = true
                        showStokDialog = true
                    },
                    onStokKeluar = {
                        itemForStokTransaksi = barang
                        isStokMasukMode = false
                        showStokDialog = true
                    },
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
    onStokMasuk: () -> Unit,
    onStokKeluar: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val placeholderPainter = rememberVectorPainter(Icons.Default.Image)
    val errorPainter = rememberVectorPainter(Icons.Default.Inventory)

    val badgeColors = when (status) {
        "Tersedia" -> Pair(Color(0xFFE8F5E9), Color(0xFF2E7D32))
        "Stok Rendah" -> Pair(Color(0xFFFFF3E0), Color(0xFFE65100))
        "Habis" -> Pair(Color(0xFFFFEBEE), Color(0xFFD32F2F))
        else -> Pair(Color(0xFFF5F5F5), Color.Gray)
    }

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
                    Box(
                        modifier = Modifier.size(54.dp).background(Color(0xFFEEEEEE), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        coil.compose.AsyncImage(
                            model = coil.request.ImageRequest.Builder(LocalContext.current)
                                .data(imageUrl.ifBlank { null })
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop,
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

                Card(colors = CardDefaults.cardColors(containerColor = badgeColors.first), shape = RoundedCornerShape(8.dp)) {
                    Text(status, color = badgeColors.second, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6)), shape = RoundedCornerShape(6.dp)) {
                    Text(category, color = Color(0xFF3F1A9C), fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                }
                Text("Stok: ", color = Color.Gray, fontSize = 12.sp)
                Text(stock, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(price, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    IconButton(onClick = onStokMasuk, modifier = Modifier.size(32.dp).background(Color(0xFFE8F0FE), CircleShape)) {
                        Icon(Icons.Default.NorthEast, contentDescription = "Masuk", modifier = Modifier.size(16.dp), tint = Color(0xFF0D47A1))
                    }
                    IconButton(onClick = onStokKeluar, modifier = Modifier.size(32.dp).background(Color(0xFFFFEBEE), CircleShape)) {
                        Icon(Icons.Default.SouthEast, contentDescription = "Keluar", modifier = Modifier.size(16.dp), tint = Color(0xFFD32F2F))
                    }
                    IconButton(onClick = onEdit, modifier = Modifier.size(32.dp).background(Color(0xFFF5F7FA), CircleShape)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(16.dp))
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp).background(Color(0xFFF5F7FA), CircleShape)) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus", modifier = Modifier.size(16.dp), tint = Color.Red)
                    }
                }
            }
        }
    }
}
