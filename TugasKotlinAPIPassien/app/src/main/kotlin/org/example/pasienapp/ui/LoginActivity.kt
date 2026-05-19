package org.example.pasienapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.example.pasienapp.R
import org.example.pasienapp.api.ApiClient
import org.example.pasienapp.data.LoginRequest
import org.example.pasienapp.data.TokenManager

class LoginActivity : AppCompatActivity() {
    
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvError: TextView
    private lateinit var tokenManager: TokenManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        tokenManager = TokenManager(this)
        
        initViews()
        setupClickListeners()
        
        // Check if user already logged in
        checkExistingToken()
    }
    
    private fun initViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressBar = findViewById(R.id.progressBar)
        tvError = findViewById(R.id.tvError)
    }
    
    private fun setupClickListeners() {
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            
            if (validateInput(email, password)) {
                performLogin(email, password)
            }
        }
    }
    
    private fun validateInput(email: String, password: String): Boolean {
        when {
            email.isEmpty() -> {
                tvError.text = "Email tidak boleh kosong"
                return false
            }
            password.isEmpty() -> {
                tvError.text = "Password tidak boleh kosong"
                return false
            }
            !email.contains("@") -> {
                tvError.text = "Format email tidak valid"
                return false
            }
            else -> {
                tvError.text = ""
                return true
            }
        }
    }
    
    private fun performLogin(email: String, password: String) {
        lifecycleScope.launch {
            try {
                progressBar.visibility = android.view.View.VISIBLE
                btnLogin.isEnabled = false
                tvError.text = ""
                
                val apiService = ApiClient.getApiService()
                val loginRequest = LoginRequest(email, password)
                val response = apiService.login(loginRequest)
                
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    val loginData = apiResponse.data // Ekstrak isi dari 'data'
                    
                    if (loginData != null) {
                        // Tambahkan safe operator (?.) untuk mencegah crash jika ada data yang hilang dari API
                        tokenManager.saveToken(
                            loginData.token,
                            loginData.user?.id?.toString() ?: "0",
                            loginData.user?.name ?: "Pengguna"
                        )
                        
                        Toast.makeText(this@LoginActivity, "Login Berhasil!", Toast.LENGTH_SHORT).show()
                        
                        // Navigate to MainActivity
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        tvError.text = "Format balasan API tidak sesuai"
                    }
                } else {
                    tvError.text = "Email atau password salah"
                }
            } catch (e: Exception) {
                tvError.text = "Error: ${e.message ?: "Terjadi kesalahan"}"
            } finally {
                progressBar.visibility = android.view.View.GONE
                btnLogin.isEnabled = true
            }
        }
    }
    
    private fun checkExistingToken() {
        lifecycleScope.launch {
            val token = tokenManager.getToken()
            if (token != null && token.isNotEmpty()) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }
}
