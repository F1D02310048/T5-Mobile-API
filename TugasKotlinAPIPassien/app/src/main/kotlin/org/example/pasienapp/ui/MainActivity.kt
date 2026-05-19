package org.example.pasienapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.example.pasienapp.R
import org.example.pasienapp.api.ApiClient
import org.example.pasienapp.data.Pasien
import org.example.pasienapp.data.TokenManager

class MainActivity : AppCompatActivity() {
    
    private lateinit var listViewPasien: ListView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnTambah: Button
    private lateinit var tokenManager: TokenManager
    private lateinit var adapter: PasienAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        tokenManager = TokenManager(this)
        initViews()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        loadPasienList()
    }
    
    private fun initViews() {
        listViewPasien = findViewById(R.id.listViewPasien)
        progressBar = findViewById(R.id.progressBar)
        tvError = findViewById(R.id.tvError)
        btnLogout = findViewById(R.id.btnLogout)
        btnTambah = findViewById(R.id.btnTambah)
        
        adapter = PasienAdapter(
            this, 
            emptyList(),
            onEditClick = { pasien -> navigateToForm(pasien) },
            onDeleteClick = { pasien -> showDeleteConfirmation(pasien) }
        )
        listViewPasien.adapter = adapter
    }
    
    private fun setupClickListeners() {
        btnLogout.setOnClickListener {
            performLogout()
        }
        btnTambah.setOnClickListener {
            navigateToForm(null)
        }
    }
    
    private fun loadPasienList() {
        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                tvError.text = ""
                
                val token = tokenManager.getToken()
                if (token == null) {
                    navigateToLogin()
                    return@launch
                }
                
                val apiService = ApiClient.getApiService()
                val response = apiService.getPasienList("Bearer $token")
                
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    val pasienList = apiResponse.data ?: emptyList()
                    
                    if (pasienList.isEmpty()) {
                        tvError.text = "Tidak ada data pasien"
                        adapter.updateData(emptyList())
                    } else {
                        adapter.updateData(pasienList)
                    }
                } else if (response.code() == 401) {
                    tvError.text = "Token expired, silakan login kembali"
                    tokenManager.clearToken()
                    navigateToLogin()
                } else {
                    tvError.text = "Gagal memuat data pasien: ${response.code()}"
                }
            } catch (e: Exception) {
                tvError.text = "Error: ${e.message ?: "Terjadi kesalahan"}"
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showDeleteConfirmation(pasien: Pasien) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Pasien")
            .setMessage("Apakah Anda yakin ingin menghapus data pasien ${pasien.nama}?")
            .setPositiveButton("Hapus") { _, _ -> performDeletePasien(pasien.id) }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun performDeletePasien(id: Int) {
        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                val token = tokenManager.getToken()
                if (token != null) {
                    val apiService = ApiClient.getApiService()
                    val response = apiService.deletePasien("Bearer $token", id)
                    
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Data pasien berhasil dihapus", Toast.LENGTH_SHORT).show()
                        loadPasienList()
                    } else {
                        Toast.makeText(this@MainActivity, "Gagal menghapus: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun navigateToForm(pasien: Pasien?) {
        val intent = Intent(this, FormPasienActivity::class.java)
        if (pasien != null) {
            intent.putExtra("IS_EDIT", true)
            intent.putExtra("PASIEN_ID", pasien.id)
            intent.putExtra("PASIEN_NAMA", pasien.nama)
            intent.putExtra("PASIEN_TANGGAL_LAHIR", pasien.tanggal_lahir) // Kirim Tanggal Lahir untuk Edit
            intent.putExtra("PASIEN_GENDER", pasien.jenis_kelamin)
            intent.putExtra("PASIEN_ALAMAT", pasien.alamat ?: "")
            intent.putExtra("PASIEN_TELEPON", pasien.no_telepon ?: "")
        } else {
            intent.putExtra("IS_EDIT", false)
        }
        startActivity(intent)
    }
    
    private fun performLogout() {
        lifecycleScope.launch {
            try {
                val token = tokenManager.getToken()
                if (token != null) {
                    val apiService = ApiClient.getApiService()
                    apiService.logout("Bearer $token")
                }
            } catch (e: Exception) {
                // Ignore
            } finally {
                tokenManager.clearToken()
                navigateToLogin()
            }
        }
    }
    
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}