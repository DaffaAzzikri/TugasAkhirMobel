# InventarisApp - Sistem Manajemen Inventaris Barang

Aplikasi manajemen inventaris berbasis Android untuk mengelola stok barang, mencatat transaksi keluar-masuk, dan memantau kondisi gudang secara real-time melalui koneksi ke backend FastAPI.

## Screenshot Aplikasi
| Login | Dashboard | Browse Barang |
| :---: | :---: | :---: |
| *(Login Screen)* | *(Dashboard Screen)* | *(Browse Screen)* |

| Tambah Barang | Riwayat Transaksi | Profil & Log |
| :---: | :---: | :---: |
| *(Form Barang)* | *(Riwayat Screen)* | *(Profil Screen)* |

## Fitur Utama
* **Inventory Management:** CRUD (Browse, Tambah, Edit, Hapus) data barang beserta foto produk.
* **Dashboard Real-time:** Menampilkan total nilai inventaris, jumlah produk tersedia, stok rendah, dan stok habis.
* **Perlu Perhatian:** Notifikasi visual untuk barang dengan stok di bawah stok minimum.
* **Transaksi Barang:** Pencatatan pergerakan barang masuk dan keluar beserta keterangan admin.
* **Riwayat:** Ringkasan transaksi dan riwayat pergerakan stok secara lengkap.
* **Manajemen Pengguna:** CRUD data user dengan role admin dan fitur aktifkan/nonaktifkan akun.
* **Log Aktivitas:** Pencatatan seluruh aktivitas yang dilakukan oleh setiap admin di sistem.

## Struktur Arsitektur
Aplikasi ini menerapkan arsitektur berlapis (**Layered Architecture**) dengan pola **MVVM** untuk memastikan pemisahan *concern* yang jelas:

* **UI Layer:** Compose Screen & ViewModel (State management dengan StateFlow).
* **Data Layer:** Repository sebagai jembatan antara Remote (API) dan Local (Room).
* **Remote:** Retrofit + OkHttp untuk komunikasi REST API ke backend FastAPI.
* **Local:** Room Database untuk caching data barang secara offline.

## Tech Stack
* **Language:** Kotlin
* **UI Framework:** Jetpack Compose + Material 3
* **Architecture:** MVVM + Layered Architecture
* **Dependency Injection:** Hilt (Dagger)
* **Networking:** Retrofit + OkHttp (FastAPI Backend)
* **Authentication:** Firebase Authentication
* **Image Upload:** Cloudinary Android SDK
* **Image Loading:** Coil
* **Local Database:** Room Database
* **Asynchronous:** Coroutines & StateFlow
* **Navigation:** Navigation Compose

## Cara Instalasi & Menjalankan
1. Clone repository ini: `git clone <url-repository>`
2. Buka folder proyek di **Android Studio**.
3. Pastikan SDK Android API 24+ (target API 36) sudah terinstal.
4. Sesuaikan `BASE_URL` di `RetrofitClient.kt` dengan alamat IP server FastAPI yang berjalan di jaringan lokal.
5. Pastikan file `google-services.json` sudah terkonfigurasi dengan benar untuk Firebase.
6. Klik tombol **Run** untuk menjalankan aplikasi pada Emulator atau Perangkat Fisik.
7. Pastikan perangkat dan server berada dalam satu jaringan yang sama.

## Informasi Tambahan
* **Backend:** REST API dibangun dengan **FastAPI** (Python), terhubung via HTTP pada jaringan lokal.
* **Autentikasi:** Menggunakan **Firebase Auth** — token ID Firebase dikirim secara otomatis ke setiap request API melalui `FirebaseAuthInterceptor` (OkHttp Interceptor).
* **Dashboard Pintar:** Menampilkan "Perlu Perhatian" untuk barang dengan `stok <= stok_minimum` dan "Pergerakan Terakhir" untuk 3 transaksi terbaru langsung dari halaman utama.
* **Role-based Access:** Model `UserModel` mendukung field `role` untuk membedakan hak akses Super Admin dan Admin biasa.
* **Offline Caching:** Room Database digunakan untuk menyimpan data barang secara lokal agar aplikasi tetap bisa menampilkan data saat koneksi terputus.

---
*Universitas Lambung Mangkurat*
