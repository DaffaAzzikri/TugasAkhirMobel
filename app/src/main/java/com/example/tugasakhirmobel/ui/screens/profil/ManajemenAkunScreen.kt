package com.example.tugasakhirmobel.ui.screens.profil

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManajemenAkunScreen(onBackClick: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("Semua") }

    val gradientHeader = Brush.horizontalGradient(listOf(Color(0xFF1565C0), Color(0xFFC62828)))

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F7FA))) {
        // --- HEADER ---
        Box(modifier = Modifier.fillMaxWidth().background(gradientHeader).padding(20.dp)) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                    Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                        Text("Manajemen Akun", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("Kelola semua pengguna sistem", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    }
                    Button(
                        onClick = { /* TODO: Dialog Tambah */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Tambah", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Stats Row
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MiniStatBox(modifier = Modifier.weight(1f), label = "Total", count = "6", color = Color(0xFF5C6BC0))
                    MiniStatBox(modifier = Modifier.weight(1f), label = "Super Admin", count = "1", color = Color(0xFFFBC02D))
                    MiniStatBox(modifier = Modifier.weight(1f), label = "Admin", count = "5", color = Color(0xFF42A5F5))
                    MiniStatBox(modifier = Modifier.weight(1f), label = "Nonaktif", count = "1", color = Color(0xFFEF5350))
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari nama atau email...", color = Color.White.copy(alpha = 0.6f)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.1f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.White
                    )
                )
            }
        }

        // --- FILTER TAB ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp).horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilterBadge(text = "Semua", isSelected = selectedTab == "Semua", onClick = { selectedTab = "Semua" })
            FilterBadge(text = "Super Admin", isSelected = selectedTab == "Super Admin", onClick = { selectedTab = "Super Admin" })
            FilterBadge(text = "Admin", isSelected = selectedTab == "Admin", onClick = { selectedTab = "Admin" })
        }

        // --- LIST USER ---
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            item { UserItemCard(name = "Super Admin", email = "superadmin@mobel.id", role = "Super Admin", status = "Aktif", isMe = true) }
            item { UserItemCard(name = "Admin Budi", email = "budi@mobel.id", role = "Admin", status = "Aktif") }
            item { UserItemCard(name = "Admin Rina", email = "rina@mobel.id", role = "Admin", status = "Aktif") }
            item { UserItemCard(name = "Admin Tono", email = "tono@mobel.id", role = "Admin", status = "Nonaktif", isActive = false) }
        }
    }
}

@Composable
fun MiniStatBox(modifier: Modifier, label: String, count: String, color: Color) {
    Box(modifier = modifier.height(65.dp).background(color.copy(alpha = 0.2f), RoundedCornerShape(12.dp)).padding(8.dp)) {
        Column {
            Text(count, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(label, color = Color.White.copy(alpha = 0.7f), fontSize = 10.sp)
        }
    }
}

@Composable
fun FilterBadge(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = if (isSelected) Color(0xFF1A237E) else Color.White,
        shape = RoundedCornerShape(20.dp),
        border = if (isSelected) null else BorderStroke(1.dp, Color.LightGray)
    ) {
        Text(text, modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp), color = if (isSelected) Color.White else Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun UserItemCard(name: String, email: String, role: String, status: String, isMe: Boolean = false, isActive: Boolean = true) {
    Card(
        modifier = Modifier.fillMaxWidth().alpha(if (isActive) 1f else 0.6f), // REDUP jika nonaktif
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(50.dp).background(Color(0xFF3F51B5), CircleShape), contentAlignment = Alignment.Center) {
                Text(name.take(2).uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(name, fontWeight = FontWeight.Bold)
                    if (isMe) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(color = Color(0xFFE3F2FD), shape = CircleShape) {
                            Text("Anda", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = Color(0xFF1976D2), fontSize = 10.sp)
                        }
                    }
                }
                Text(email, fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = if (role == "Super Admin") Color(0xFFFFF8E1) else Color(0xFFE8EAF6), shape = RoundedCornerShape(4.dp)) {
                        Text(role, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 10.sp, color = if (role == "Super Admin") Color(0xFFFBC02D) else Color(0xFF3F51B5))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Login: 12/06/2026", fontSize = 10.sp, color = Color.LightGray)
                }
            }

            Row {
                IconButton(onClick = {}) { Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(20.dp)) }
                if (!isMe) {
                    IconButton(onClick = {}) { Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red, modifier = Modifier.size(20.dp)) }
                }
            }
        }
    }
}