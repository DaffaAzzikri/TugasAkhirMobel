package com.example.tugasakhirmobel.ui.screens.barang

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormBarangScreen(
    viewModel: BarangViewModel,
    onCloseClick: () -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.barangState.collectAsState()

    var productName by remember { mutableStateOf("") }
    var currentStock by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var sku by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var supplier by remember { mutableStateOf("") }

    // State khusus untuk Dropdown Kategori
    var category by remember { mutableStateOf("") }
    val daftarKategoriForm = listOf("Elektronik", "Furnitur", "ATK", "Pakaian", "Makanan & Minuman", "Peralatan")

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> if (uri != null) imageUri = uri }
    )

    LaunchedEffect(state) {
        when (state) {
            is BarangState.Success -> {
                Toast.makeText(context, "Barang berhasil disimpan!", Toast.LENGTH_SHORT).show()
                viewModel.resetState()
                onCloseClick()
            }
            is BarangState.Error -> {
                Toast.makeText(context, (state as BarangState.Error).message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tambah Produk", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                actions = {
                    IconButton(
                        onClick = onCloseClick,
                        modifier = Modifier.padding(end = 8.dp).background(Color(0xFFF5F5F5), CircleShape).size(36.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", modifier = Modifier.size(20.dp))
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // --- 1. KOMPONEN FOTO PRODUK ---
                CustomLabel("Foto Barang *")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(Color(0xFFF1F4F9), RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(imageUri),
                            contentDescription = "Preview Gambar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Klik untuk memilih foto dari Galeri", color = Color.Gray, fontSize = 13.sp)
                        }
                    }
                }

                // --- 2. NAMA PRODUK ---
                CustomLabel("Nama Produk *")
                CustomInputField(value = productName, onValueChange = { productName = it }, placeholder = "Masukkan nama produk")

                // --- 3. SKU ---
                CustomLabel("SKU *")
                CustomInputField(value = sku, onValueChange = { sku = it }, placeholder = "Kode SKU")

                // --- 4. KATEGORI (MENGGUNAKAN DROPDOWN) ---
                CustomLabel("Kategori *")
                CustomDropdownField(
                    value = category,
                    onValueChange = { category = it },
                    options = daftarKategoriForm,
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

                // --- 6. STOK ---
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        CustomLabel("Stok Saat Ini *")
                        CustomInputField(value = currentStock, onValueChange = { currentStock = it }, placeholder = "0")
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        CustomLabel("Stok Minimum")
                        CustomInputField(value = "0", onValueChange = {}, placeholder = "0")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- 7. TOMBOL AKSI ---
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = onCloseClick,
                        enabled = state !is BarangState.Loading,
                        modifier = Modifier.weight(1f).height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(25.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Text("Batal", color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { viewModel.tambahBarangBaru(productName, currentStock, imageUri) },
                        enabled = state !is BarangState.Loading,
                        modifier = Modifier.weight(1f).height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F1A9C)),
                        shape = RoundedCornerShape(25.dp)
                    ) {
                        Text("Tambah Produk", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }

            if (state is BarangState.Loading) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
    }
}

@Composable
fun CustomLabel(text: String) {
    Text(text = text, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomInputField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
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

// --- KOMPONEN BARU UNTUK DROPDOWN MENU FORM ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownField(
    value: String,
    onValueChange: (String) -> Unit,
    options: List<String>,
    placeholder: String
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = value,
            onValueChange = {}, // Read-only karena ini dropdown
            readOnly = true,
            placeholder = { Text(placeholder, fontSize = 14.sp, color = Color.Gray) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor(), // menuAnchor penting agar menu nempel ke textfield
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

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption, color = Color.Black) },
                    onClick = {
                        onValueChange(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}