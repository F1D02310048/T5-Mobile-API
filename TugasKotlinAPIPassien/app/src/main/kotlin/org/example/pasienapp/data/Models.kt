package org.example.pasienapp.data

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: UserData
)

data class UserData(
    val id: Int,
    val name: String,
    val email: String
)

// Model Pasien disesuaikan dengan response JSON GET list terbaru
data class Pasien(
    val id: Int,
    val nama: String,
    val tanggal_lahir: String,
    val jenis_kelamin: String,
    val alamat: String? = null,
    val no_telepon: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)

data class PasienRequest(
    val nama: String,
    val tanggal_lahir: String,
    val jenis_kelamin: String,
    val alamat: String,
    val no_telepon: String
)

data class ApiResponse<T>(
    val success: Boolean? = null,
    val message: String? = null,
    val data: T? = null,
    val status: String? = null
)