package com.example.tugasakhirmobel.ui.screens.profil

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManajemenAkunScreen(onBackClick: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableStateOf("Semua") }

    // State untuk mengontrol muncul/hilangnya pop-up form tambah pengguna
    var showTambahDialog by remember { mutableStateOf(false) }

    val gradientHeader = Brush.verticalGradient(listOf(Color(0xFF6A1B9A), Color(0xFFC62828)))

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F7FA))) {
        // --- 1. HEADER (SUDAH DIRAPIKAN PADDING & ALIGNMENT-NYA) ---
        Box(modifier = Modifier.fillMaxWidth().background(gradientHeader).padding(top = 32.dp, bottom = 24.dp, start = 20.dp, end = 20.dp)) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.size(36.dp) // Ukuran tombol diperbaiki
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Manajemen Akun", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("Kelola semua pengguna sistem", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                    }
                    Button(
                        onClick = { showTambahDialog = true }, // Menampilkan Dialog
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Tambah", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

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

        // --- 2. FILTER TAB ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp).horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilterBadge(text = "Semua", isSelected = selectedTab == "Semua", onClick = { selectedTab = "Semua" })
            FilterBadge(text = "Super Admin", isSelected = selectedTab == "Super Admin", onClick = { selectedTab = "Super Admin" })
            FilterBadge(text = "Admin", isSelected = selectedTab == "Admin", onClick = { selectedTab = "Admin" })
        }

        // --- 3. LIST USER ---
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

    // --- 4. PEMANGGILAN DIALOG TAMBAH PENGGUNA ---
    if (showTambahDialog) {
        TambahPenggunaDialog(
            onDismiss = { showTambahDialog = false },
            onSave = {
                // TODO: Logika save ke ViewModel Daffa nanti
                showTambahDialog = false
            }
        )
    }
}

// ==========================================
// KOMPONEN DIALOG FORM TAMBAH PENGGUNA BARU
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahPenggunaDialog(onDismiss: () -> Unit, onSave: () -> Unit) {
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Admin") } // Default Admin
    var isAktif by remember { mutableStateOf(true) }
    var passwordVisible by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Dialog
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Tambah Pengguna", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Tutup", tint = Color.Gray)
                    }
                }

                // Field Nama Lengkap
                Column {
                    Text("Nama Lengkap *", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
                    DialogTextField(value = nama, onValueChange = { nama = it }, placeholder = "Masukkan nama lengkap")
                }

                // Field Email
                Column {
                    Text("Email *", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
                    DialogTextField(value = email, onValueChange = { email = it }, placeholder = "email@domain.com")
                }

                // Field Password
                Column {
                    Text("Password *", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 4.dp))
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Masukkan password", fontSize = 14.sp, color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF1F4F9),
                            unfocusedContainerColor = Color(0xFFF1F4F9),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        }
                    )
                }

                // Pemilihan Role
                Column {
                    Text("Role", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        RoleSelectionBox(
                            modifier = Modifier.weight(1f),
                            text = "Admin",
                            icon = Icons.Default.VerifiedUser,
                            isSelected = role == "Admin",
                            selectedColor = Color(0xFF0D47A1),
                            onClick = { role = "Admin" }
                        )
                        RoleSelectionBox(
                            modifier = Modifier.weight(1f),
                            text = "Super Admin",
                            icon = Icons.Default.WorkspacePremium,
                            isSelected = role == "Super Admin",
                            selectedColor = Color(0xFFE65100),
                            onClick = { role = "Super Admin" }
                        )
                    }
                }

                // Switch Status Akun
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Status Akun", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(if (isAktif) "Akun aktif dan dapat login" else "Akun dibekukan sementara", fontSize = 12.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = isAktif,
                        onCheckedChange = { isAktif = it },
                        colors = SwitchDefaults.colors(checkedTrackColor = Color(0xFF1565C0))
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Tombol Aksi
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.LightGray)
                    ) {
                        Text("Batal", color = Color.Black)
                    }
                    Button(
                        onClick = onSave,
                        modifier = Modifier.weight(1f).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)), // Menggunakan warna utama tema
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Tambah", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// --- Komponen Bantuan ---
@Composable
fun DialogTextField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, fontSize = 14.sp, color = Color.Gray) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF1F4F9),
            unfocusedContainerColor = Color(0xFFF1F4F9),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true
    )
}

@Composable
fun RoleSelectionBox(modifier: Modifier, text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, selectedColor: Color, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .clickable { onClick() }
            .background(if (isSelected) selectedColor.copy(alpha = 0.1f) else Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, if (isSelected) selectedColor else Color.LightGray, RoundedCornerShape(12.dp))
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = if (isSelected) selectedColor else Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text, color = if (isSelected) selectedColor else Color.Gray, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, fontSize = 13.sp)
        }
    }
}

@Composable
fun MiniStatBox(modifier: Modifier, label: String, count: String, color: Color) {
    Box(modifier = modifier.height(65.dp).background(color.copy(alpha = 0.2f), RoundedCornerShape(12.dp)).padding(8.dp)) {
        Column {
            Text(count, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(label, color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp)
        }
    }
}

@Composable
fun FilterBadge(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = if (isSelected) Color(0xFF1565C0) else Color.White,
        shape = RoundedCornerShape(20.dp),
        border = if (isSelected) null else BorderStroke(1.dp, Color.LightGray)
    ) {
        Text(text, modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp), color = if (isSelected) Color.White else Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun UserItemCard(name: String, email: String, role: String, status: String, isMe: Boolean = false, isActive: Boolean = true) {
    Card(
        modifier = Modifier.fillMaxWidth().alpha(if (isActive) 1f else 0.6f),
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