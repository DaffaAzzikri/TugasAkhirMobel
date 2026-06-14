package com.example.tugasakhirmobel.ui.screens.log

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

fun getLogProperties(actionText: String): Pair<ImageVector, Color> {
    val text = actionText.lowercase()
    return when {
        text.contains("menambahkan barang") -> Pair(Icons.Default.Add, Color(0xFF10B981))
        text.contains("transaksi masuk") -> Pair(Icons.Default.NorthEast, Color(0xFF3B82F6))
        text.contains("transaksi keluar") -> Pair(Icons.Default.SouthEast, Color(0xFFEF4444))
        text.contains("mengedit barang") -> Pair(Icons.Default.Edit, Color(0xFF8B5CF6))
        text.contains("menghapus barang") -> Pair(Icons.Default.Delete, Color(0xFFEF4444))
        text.contains("memperbarui data") -> Pair(Icons.Default.Person, Color(0xFFF59E0B)) // Warna Amber untuk user update
        else -> Pair(Icons.Default.Info, Color.Gray)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(viewModel: LogViewModel = hiltViewModel()) {
    val logList by viewModel.logList.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }

    val gradientHeader = Brush.verticalGradient(
        colors = listOf(Color(0xFF6A1B9A), Color(0xFFC62828))
    )

    // PERBAIKAN: Logika Filter menggunakan contains() untuk mencocokkan kategori
    val filteredLogs = logList.filter { log ->
        val matchesSearch = log.aksi.contains(searchQuery, ignoreCase = true) ||
                log.namaAdmin.contains(searchQuery, ignoreCase = true)

        val action = log.aksi.lowercase()
        val matchesFilter = when (selectedFilter) {
            "Semua" -> true
            "Tambah Barang" -> action.contains("menambahkan barang")
            "Edit Barang" -> action.contains("mengedit barang")
            "Hapus Barang" -> action.contains("menghapus barang")
            "Stok Masuk" -> action.contains("transaksi masuk")
            "Stok Keluar" -> action.contains("transaksi keluar")
            else -> true
        }

        matchesSearch && matchesFilter
    }

    val groupedLogs = filteredLogs.groupBy { log ->
        if (log.createdAt.contains(" ")) log.createdAt.substringBefore(" ") else log.createdAt
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F7FA)),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth().background(gradientHeader).padding(horizontal = 20.dp, vertical = 24.dp)) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Shield, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Log Admin", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    }
                    Text("Riwayat semua perubahan oleh administrator", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(20.dp))
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari aksi atau admin...", color = Color.White.copy(alpha = 0.6f)) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White.copy(alpha = 0.7f)) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White.copy(alpha = 0.15f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.15f),
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White
                        ),
                        singleLine = true
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp).horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("Semua", "Tambah Barang", "Edit Barang", "Hapus Barang", "Stok Masuk", "Stok Keluar").forEach { filter ->
                    FilterTab(text = filter, isSelected = selectedFilter == filter, onClick = { selectedFilter = filter }, isGradient = filter == "Semua")
                }
            }
        }

        if (groupedLogs.isEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                    Text(text = "Tidak ada riwayat log ditemukan", color = Color.Gray, fontSize = 14.sp)
                }
            }
        } else {
            groupedLogs.forEach { (date, logsInDate) ->
                item {
                    Text(text = date, fontSize = 13.sp, color = Color.Gray, modifier = Modifier.padding(start = 20.dp, top = 8.dp, bottom = 4.dp))
                }
                items(logsInDate) { log ->
                    val (icon, color) = getLogProperties(log.aksi)
                    val timeOnly = if (log.createdAt.contains(" ")) log.createdAt.substringAfter(" ") else log.createdAt
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                        LogItemCard(
                            actionType = log.aksi,
                            operator = log.namaAdmin,
                            time = timeOnly,
                            icon = icon,
                            tintColor = color
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterTab(text: String, isSelected: Boolean, onClick: () -> Unit, isGradient: Boolean = false) {
    val gradientBrush = Brush.horizontalGradient(listOf(Color(0xFF3F1A9C), Color(0xFFC62828)))
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .then(
                if (isSelected && isGradient) {
                    Modifier.background(gradientBrush)
                } else {
                    Modifier.background(
                        if (isSelected) Color.White else Color.Transparent
                    )
                }
            )
            .border(1.dp, if (isSelected) Color.Transparent else Color.LightGray, RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = if (isSelected && isGradient) Color.White else if (isSelected) Color.Black else Color.Gray, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

@Composable
fun LogItemCard(actionType: String, operator: String, time: String, icon: ImageVector, tintColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(48.dp).background(tintColor.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = null, tint = tintColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = actionType, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = operator, fontSize = 12.sp, color = Color.Gray)
                    Text(text = " • ", color = Color.LightGray)
                    Text(text = time, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}
