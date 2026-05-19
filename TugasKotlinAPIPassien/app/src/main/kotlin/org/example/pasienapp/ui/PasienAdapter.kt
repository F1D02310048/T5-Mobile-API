package org.example.pasienapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import org.example.pasienapp.R
import org.example.pasienapp.data.Pasien

class PasienAdapter(
    private val context: Context,
    private var pasienList: List<Pasien>,
    private val onEditClick: (Pasien) -> Unit,
    private val onDeleteClick: (Pasien) -> Unit
) : BaseAdapter() {
    
    override fun getCount(): Int = pasienList.size
    
    override fun getItem(position: Int): Any = pasienList[position]
    
    override fun getItemId(position: Int): Long = position.toLong()
    
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_pasien, parent, false)
        
        val pasien = pasienList[position]
        
        view.findViewById<TextView>(R.id.tvNama).text = pasien.nama
        view.findViewById<TextView>(R.id.tvTanggalLahir).text = pasien.tanggal_lahir
        
        // Konversi inisial satu huruf dari API menjadi teks rapi di UI list
        val genderText = when (pasien.jenis_kelamin) {
            "L" -> "Laki-laki"
            "P" -> "Perempuan"
            else -> pasien.jenis_kelamin
        }
        view.findViewById<TextView>(R.id.tvJenisKelamin).text = genderText
        
        view.findViewById<Button>(R.id.btnEdit).setOnClickListener {
            onEditClick(pasien)
        }
        
        view.findViewById<Button>(R.id.btnDelete).setOnClickListener {
            onDeleteClick(pasien)
        }
        
        return view
    }
    
    fun updateData(newList: List<Pasien>) {
        pasienList = newList
        notifyDataSetChanged()
    }
}