package com.example.tugasakhirmobel.ui.screens.profil

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit,
    onManageAccountClick: () -> Unit,
    viewModel: ProfilViewModel = hiltViewModel()
) {
    val profileState by viewModel.profileState.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCurrentUser()
    }

    when (val state = profileState) {
        ProfilState.Idle, ProfilState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F7FA)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF6A1B9A))
            }
        }
        is ProfilState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F7FA))
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.ErrorOutline,
                        contentDescription = null,
                        tint = Color(0xFFC62828),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        state.message,
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { viewModel.loadCurrentUser() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A))
                    ) {
                        Text("Coba Lagi")
                    }
                }
            }
        }
        ProfilState.Success -> {
            currentUser?.let { user ->
                ProfileContent(
                    userName = user.nama.orEmpty(),
                    userEmail = user.email.orEmpty(),
                    userRole = user.role.orEmpty(),
                    createdAt = formatCreatedAt(user.createdAt),
                    isSuperAdmin = user.role?.equals("Super Admin", ignoreCase = true) == true,
                    onLogoutClick = onLogoutClick,
                    onManageAccountClick = onManageAccountClick
                )
            } ?: run {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F7FA)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Data profil tidak tersedia.", color = Color.Gray)
                }
            }
        }
    }
}

@Composable
private fun ProfileContent(
    userName: String,
    userEmail: String,
    userRole: String,
    createdAt: String,
    isSuperAdmin: Boolean,
    onLogoutClick: () -> Unit,
    onManageAccountClick: () -> Unit
) {
    val avatarInitials = getInitialsFromName(userName)

    val gradientPurple = Brush.verticalGradient(
        colors = listOf(Color(0xFF6A1B9A), Color(0xFFE91E63))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(Brush.verticalGradient(listOf(Color(0xFF6A1B9A), Color(0xFFC62828)))),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            avatarInitials,
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(userName, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(userEmail, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = Color(0xFFFFD600),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = Color(0xFF1A237E),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            userRole,
                            color = Color(0xFF1A237E),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {
            Text("DETAIL AKUN", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* TODO: Buka dialog ganti nama */ }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF3F51B5),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Nama Lengkap", modifier = Modifier.weight(1f), fontSize = 14.sp, color = Color.Gray)
                        Text(userName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Nama",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF5F7FA))

                    DetailRow(label = "Email", value = userEmail, icon = Icons.Default.Email)
                    DetailRow(label = "Role", value = userRole, icon = Icons.Default.AccountCircle)
                    DetailRow(label = "Akun Dibuat", value = createdAt, icon = Icons.Default.CalendarToday)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isSuperAdmin) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(gradientPurple)
                        .clickable { onManageAccountClick() }
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Group, contentDescription = null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Manajemen Akun", color = Color.White, fontWeight = FontWeight.Bold)
                            Text(
                                "Kelola semua pengguna sistem",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        "UTS Mobel Lejen v1.0.0\nSistem Manajemen Inventaris — 2026",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                border = BorderStroke(1.dp, Color.Red),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, tint = Color.Red)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Keluar dari Akun", color = Color.Red, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, modifier = Modifier.weight(1f), fontSize = 14.sp, color = Color.Gray)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

private fun getInitialsFromName(nama: String?): String {
    if (nama.isNullOrBlank()) return "?"
    return nama.trim()
        .split("\\s+".toRegex())
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { word -> word.first().uppercaseChar().toString() }
        .ifEmpty { "?" }
}

private fun formatCreatedAt(isoDate: String?): String {
    if (isoDate.isNullOrBlank()) return "—"
    val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val patterns = listOf(
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd")
    )
    for (pattern in patterns) {
        try {
            return LocalDateTime.parse(isoDate, pattern).format(outputFormatter)
        } catch (_: DateTimeParseException) {
            // coba pattern berikutnya
        }
    }
    return isoDate.substringBefore('T').let { datePart ->
        try {
            LocalDateTime.parse("${datePart}T00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                .format(outputFormatter)
        } catch (_: DateTimeParseException) {
            "—"
        }
    }
}
