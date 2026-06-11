package com.example.tugasakhirmobel.ui.screens.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormScreen(
    onCloseClick: () -> Unit
) {
    var photoUrl by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var sku by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("0") }
    var supplier by remember { mutableStateOf("") }
    var currentStock by remember { mutableStateOf("0") }
    var minimumStock by remember { mutableStateOf("0") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tambah Produk", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                actions = {
                    IconButton(
                        onClick = onCloseClick,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .background(Color(0xFFF5F5F5), CircleShape)
                            .size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- 1. FOTO BARANG (URL) ---
            CustomLabel("Foto Barang (URL gambar)")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color(0xFFF1F4F9), RoundedCornerShape(28.dp))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                BasicTextField(
                    value = photoUrl,
                    onValueChange = { photoUrl = it },
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField ->
                        if (photoUrl.isEmpty()) Text("https://...", color = Color.Gray, fontSize = 14.sp)
                        innerTextField()
                    }
                )
            }

            // --- 2. NAMA PRODUK ---
            CustomLabel("Nama Produk *")
            CustomInputField(
                value = productName,
                onValueChange = { productName = it },
                placeholder = "Masukkan nama produk"
            )

            // --- 3. SKU ---
            CustomLabel("SKU *")
            CustomInputField(
                value = sku,
                onValueChange = { sku = it },
                placeholder = "Kode SKU"
            )

            // --- 4. KATEGORI ---
            CustomLabel("Kategori *")
            CustomInputField(
                value = category,
                onValueChange = { category = it },
                placeholder = "Pilih kategori"
            )

            // --- 5. HARGA & SUPPLIER ---
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    CustomLabel("Harga (Rp)")
                    CustomInputField(value = price, onValueChange = { price = it }, placeholder = "0")
                }
                Column(modifier = Modifier.weight(1f)) {
                    CustomLabel("Supplier")
                    CustomInputField(value = supplier, onValueChange = { supplier = it }, placeholder = "Nama supplier")
                }
            }

            // --- 6. STOK SAAT INI & STOK MINIMUM ---
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    CustomLabel("Stok Saat Ini")
                    CustomInputField(value = currentStock, onValueChange = { currentStock = it }, placeholder = "0")
                }
                Column(modifier = Modifier.weight(1f)) {
                    CustomLabel("Stok Minimum")
                    CustomInputField(value = minimumStock, onValueChange = { minimumStock = it }, placeholder = "0")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 7. TOMBOL AKSI ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onCloseClick,
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(25.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text("Batal", color = Color.Black, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { /* Submit logic */ },
                    modifier = Modifier.weight(1f).height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9FA8DA)),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text("Tambah Produk", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun CustomLabel(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        color = Color.Gray,
        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, fontSize = 14.sp, color = Color.Gray) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF1F4F9),
            unfocusedContainerColor = Color(0xFFF1F4F9),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        singleLine = true
    )
}

// Tambahkan impor ini di bagian atas:
//