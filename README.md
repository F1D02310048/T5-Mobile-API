# 🏥 Aplikasi Mobile Pasien - Android Kotlin

Aplikasi mobile berbasis Android menggunakan **Kotlin** yang terintegrasi dengan **REST API** untuk mengelola data pasien.  
Aplikasi menerapkan sistem autentikasi menggunakan **Bearer Token**, penyimpanan token dengan **DataStore**, serta komunikasi API menggunakan **Retrofit**.

---

# 👤 Identitas Mahasiswa

- **Nama:** Fadila Rahmania
- **NIM:** F1D02310048

---

# 📱 Deskripsi Aplikasi

Aplikasi ini digunakan untuk:

- Login pengguna menggunakan API
- Menyimpan token autentikasi
- Menampilkan daftar pasien dari server
- Mengelola sesi login/logout
- Menghubungkan aplikasi Android dengan REST API menggunakan Retrofit

Data pasien diambil dari endpoint API dengan mekanisme:

```http
Authorization: Bearer <token>
```

---

# ✨ Fitur Aplikasi

✅ Sistem Login menggunakan email & password  
✅ Integrasi REST API  
✅ Penyimpanan token menggunakan DataStore  
✅ Menampilkan daftar pasien  
✅ Tambah data pasien  
✅ Edit data pasien  
✅ Hapus data pasien  
✅ Logout dan hapus token  
✅ Loading indicator  
✅ Error handling  
✅ Authentication menggunakan Bearer Token  

---

# 📸 Screenshot Aplikasi

## 🔐 Halaman Login

<img width="409" height="862" alt="WhatsApp Image 2026-05-18 at 23 51 30" src="https://github.com/user-attachments/assets/55287053-a042-4bb2-a406-1e1edf3e235a" />


## 📋 Halaman Data Pasien

<img width="409" height="862" alt="WhatsApp Image 2026-05-18 at 23 51 30(1)" src="https://github.com/user-attachments/assets/1faf773c-28ac-415a-b68a-b9ab2e04e9e7" />


## ➕ Tambah Data Pasien

<img width="409" height="862" alt="WhatsApp Image 2026-05-18 at 23 51 30(2)" src="https://github.com/user-attachments/assets/b872cd60-4ddf-4e3d-8306-2df3b06c9b69" />


## ✏️ Edit Data Pasien

<img width="409" height="862" alt="WhatsApp Image 2026-05-18 at 23 51 30(3)" src="https://github.com/user-attachments/assets/42dadbdd-88e1-41ce-a790-c1a3183a0c35" />


## ✏️ Hapus Data Pasien

<img width="409" height="862" alt="WhatsApp Image 2026-05-18 at 23 51 30(4)" src="https://github.com/user-attachments/assets/f4709636-1cb2-4edb-83c0-109fbb48dc9d" />

## 📋 Halaman Data Pasien Update

<img width="409" height="862" alt="WhatsApp Image 2026-05-18 at 23 51 31" src="https://github.com/user-attachments/assets/a45881a0-ad1f-44d8-a410-67db6ed4f0fa" />


---

# 🚀 Cara Menjalankan Project

## 1. Clone Repository

```bash
git clone https://github.com/F1D02310048/T5-Mobile-API.git
```

---

## 2. Buka di Android Studio

- Open Project
- Pilih folder project
- Tunggu Gradle Sync selesai

---

## 3. Jalankan Aplikasi

Gunakan emulator atau Android device:

```bash
./gradlew installDebug
```

atau klik tombol **Run** di Android Studio.

---

