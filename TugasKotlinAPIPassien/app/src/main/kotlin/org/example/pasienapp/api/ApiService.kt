package org.example.pasienapp.api

import org.example.pasienapp.data.LoginRequest
import org.example.pasienapp.data.LoginResponse
import org.example.pasienapp.data.Pasien
import org.example.pasienapp.data.PasienRequest
import org.example.pasienapp.data.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiService {
    
    @POST("api/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>
    
    @POST("api/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<ApiResponse<Any>>
    
    @GET("api/pasien")
    suspend fun getPasienList(@Header("Authorization") token: String): Response<ApiResponse<List<Pasien>>>

    @POST("api/pasien")
    suspend fun addPasien(
        @Header("Authorization") token: String,
        @Body pasien: PasienRequest // Diubah menggunakan PasienRequest
    ): Response<ApiResponse<Pasien>>

    @PUT("api/pasien/{id}")
    suspend fun updatePasien(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body pasien: PasienRequest // Diubah menggunakan PasienRequest
    ): Response<ApiResponse<Pasien>>

    @DELETE("api/pasien/{id}")
    suspend fun deletePasien(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<ApiResponse<Any>>
}