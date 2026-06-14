package com.example.tugasakhirmobel.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DashboardScreen(
    onLogoutClick: () -> Unit,
    onBrowseClick: () -> Unit,
    onRiwayatClick: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val dashboardData by viewModel.dashboardData.collectAsState()
    val perluPerhatian by viewModel.perluPerhatian.collectAsState()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDashboard()
    }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF3F1A9C), Color(0xFFE91E63))
    )

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7FA)),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // --- 1. HEADER ---
            item {
                Box(modifier = Modifier.fillMaxWidth().background(gradientBackground).padding(24.dp)) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Selamat datang,", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                                Text("Super Admin", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }
                            IconButton(onClick = onLogoutClick, modifier = Modifier.background(Color.White.copy(alpha = 0.2f), CircleShape)) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout", tint = Color.White)
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)), shape = RoundedCornerShape(16.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Total Nilai Inventaris", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                                val formattedValue = NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(dashboardData?.totalNilaiInventaris ?: 0)
                                Text(formattedValue, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // --- 2. STATS CARD ---
            item {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).offset(y = (-20).dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatCard(modifier = Modifier.weight(1f), title = "Tersedia", count = "${dashboardData?.tersedia ?: 0}", icon = Icons.Default.Inventory, iconColor = Color(0xFF0D47A1))
                    StatCard(modifier = Modifier.weight(1f), title = "Stok Rendah", count = "${dashboardData?.stokRendah ?: 0}", icon = Icons.Default.Warning, iconColor = Color(0xFFE65100))
                    StatCard(modifier = Modifier.weight(1f), title = "Habis", count = "${dashboardData?.habis ?: 0}", icon = Icons.Default.NewReleases, iconColor = Color(0xFFB71C1C))
                }
            }

            // --- 3. PERLU PERHATIAN ---
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Perlu Perhatian", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        TextButton(onClick = onBrowseClick) { Text("Lihat Semua") }
                    }
                }
            }

            if (perluPerhatian.isEmpty() && state is DashboardState.Idle) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("Tidak ada barang yang memerlukan perhatian.", color = Color.Gray, fontSize = 14.sp)
                    }
                }
            } else {
                items(perluPerhatian) { barang ->
                    val isCritical = barang.stok <= 0
                    val tag = if (isCritical) "Habis" else "Stok Rendah"
                    AlertItem(
                        name = barang.namaBarang,
                        info = "Stok: ${barang.stok} / Min: ${barang.stokMinimum}",
                        tag = tag,
                        isCritical = isCritical
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // --- 4. PERGERAKAN TERAKHIR ---
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text("Pergerakan Terakhir", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onRiwayatClick() }) {
                                Text("Lihat semua", fontSize = 12.sp, color = Color(0xFF0D47A1))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        MovementItem(icon = Icons.Default.CallMade, iconTint = Color(0xFF0D47A1), title = "Laptop Asus", date = "09/06/2026", amount = "+5", amountColor = Color(0xFF0D47A1))
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                        MovementItem(icon = Icons.Default.CallReceived, iconTint = Color(0xFFC62828), title = "Mouse Wireless", date = "08/06/2026", amount = "-3", amountColor = Color(0xFFC62828))
                    }
                }
            }

            // --- 5. MENU BAWAH ---
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Browse
                    Card(onClick = onBrowseClick, modifier = Modifier.weight(1f).height(75.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF0D47A1))) {
                        Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                            Text("Browse Barang", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("${dashboardData?.totalProduk ?: 0} produk", color = Color.White.copy(0.7f), fontSize = 12.sp)
                        }
                    }
                    // Riwayat
                    Card(onClick = onRiwayatClick, modifier = Modifier.weight(1f).height(75.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFC62828))) {
                        Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
                            Text("Riwayat", color = Color.White, fontWeight = FontWeight.Bold)
                            Text("8 transaksi", color = Color.White.copy(0.7f), fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        if (state is DashboardState.Loading) {
            Box(modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.5f)), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF3F1A9C))
            }
        }

        if (state is DashboardState.Error) {
            Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text((state as DashboardState.Error).message, color = Color.Red, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    Button(onClick = { viewModel.loadDashboard() }, modifier = Modifier.padding(top = 8.dp)) {
                        Text("Coba Lagi")
                    }
                }
            }
        }
    }
}

@Composable
fun MovementItem(icon: ImageVector, iconTint: Color, title: String, date: String, amount: String, amountColor: Color) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(40.dp).background(iconTint.copy(alpha = 0.1f), CircleShape), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(date, fontSize = 12.sp, color = Color.Gray)
        }
        Text(amount, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = amountColor)
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, title: String, count: String, icon: ImageVector, iconColor: Color) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
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
