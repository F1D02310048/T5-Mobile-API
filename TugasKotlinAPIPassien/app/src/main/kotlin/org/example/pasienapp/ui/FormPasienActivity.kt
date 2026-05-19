package org.example.pasienapp.ui

import android.os.Bundle
import android.view.View
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
import org.example.pasienapp.data.PasienRequest
import org.example.pasienapp.data.TokenManager

class FormPasienActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var etNama: EditText
    private lateinit var etTanggalLahir: EditText
    private lateinit var etJenisKelamin: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etTelepon: EditText
    private lateinit var btnSimpan: Button
    private lateinit var progressBar: ProgressBar
    
    private lateinit var tokenManager: TokenManager
    private var isEditMode = false
    private var pasienId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_pasien)

        tokenManager = TokenManager(this)
        initViews()
        checkIntentData()

        btnSimpan.setOnClickListener {
            processSave()
        }
    }

    private fun initViews() {
        tvTitle = findViewById(R.id.tvFormTitle)
        etNama = findViewById(R.id.etNama)
        etTanggalLahir = findViewById(R.id.etTanggalLahir)
        etJenisKelamin = findViewById(R.id.etJenisKelamin)
        etAlamat = findViewById(R.id.etAlamat)
        etTelepon = findViewById(R.id.etTelepon)
        btnSimpan = findViewById(R.id.btnSimpan)
        progressBar = findViewById(R.id.formProgressBar)
    }

    private fun checkIntentData() {
        isEditMode = intent.getBooleanExtra("IS_EDIT", false)
        if (isEditMode) {
            tvTitle.text = "Edit Data Pasien"
            btnSimpan.text = "Update Data"
            btnSimpan.backgroundTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#2196F3"))
            
            pasienId = intent.getIntExtra("PASIEN_ID", -1)
            etNama.setText(intent.getStringExtra("PASIEN_NAMA"))
            etTanggalLahir.setText(intent.getStringExtra("PASIEN_TANGGAL_LAHIR")) // Tampilkan Tanggal Lahir lama saat Edit
            etJenisKelamin.setText(intent.getStringExtra("PASIEN_GENDER"))
            etAlamat.setText(intent.getStringExtra("PASIEN_ALAMAT"))
            etTelepon.setText(intent.getStringExtra("PASIEN_TELEPON"))
        } else {
            tvTitle.text = "Tambah Pasien Baru"
            btnSimpan.text = "Simpan Baru"
        }
    }

    private fun processSave() {
        val nama = etNama.text.toString().trim()
        val tanggalLahir = etTanggalLahir.text.toString().trim()
        val genderInput = etJenisKelamin.text.toString().trim().uppercase()
        val alamat = etAlamat.text.toString().trim()
        val telepon = etTelepon.text.toString().trim()

        val gender = if (genderInput.startsWith("P")) "P" else "L"

        if (nama.isEmpty() || tanggalLahir.isEmpty() || alamat.isEmpty() || telepon.isEmpty()) {
            Toast.makeText(this, "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        val pasienRequest = PasienRequest(
            nama = nama,
            tanggal_lahir = tanggalLahir,
            jenis_kelamin = gender,
            alamat = alamat,
            no_telepon = telepon
        )

        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                btnSimpan.isEnabled = false
                val token = tokenManager.getToken()
                
                if (token == null) {
                    Toast.makeText(this@FormPasienActivity, "Sesi habis, silakan login ulang", Toast.LENGTH_SHORT).show()
                    finish()
                    return@launch
                }

                val apiService = ApiClient.getApiService()
                val response = if (isEditMode) {
                    apiService.updatePasien("Bearer $token", pasienId, pasienRequest)
                } else {
                    apiService.addPasien("Bearer $token", pasienRequest)
                }

                if (response.isSuccessful) {
                    Toast.makeText(this@FormPasienActivity, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(this@FormPasienActivity, "Gagal ${response.code()}", Toast.LENGTH_LONG).show()
                    println("API_ERROR_VALIDATION: $errorBody")
                }
            } catch (e: Exception) {
                Toast.makeText(this@FormPasienActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                progressBar.visibility = View.GONE
                btnSimpan.isEnabled = true
            }
        }
    }
}