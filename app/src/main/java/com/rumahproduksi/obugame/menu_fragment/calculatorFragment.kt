package com.rumahproduksi.obugame.menu_fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rumahproduksi.obugame.R
import com.rumahproduksi.obugame.SettingActivity
import com.rumahproduksi.obugame.adapter.logic.EOQCalculatorAdapter
import com.rumahproduksi.obugame.databinding.FragmentCalculatorBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.math.round

class calculatorFragment : Fragment() {
    lateinit var binding: FragmentCalculatorBinding
    private val eoqCalculatorAdapter = EOQCalculatorAdapter()
    private lateinit var mDbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalculatorBinding.inflate(inflater, container, false)

        val inputTanggal = binding.root.findViewById<EditText>(R.id.tanggal_input)
        val inputBahanBaku = binding.root.findViewById<EditText>(R.id.input_bahanbaku)
        val inputKemasanTerpakai = binding.root.findViewById<EditText>(R.id.input_kemasanterpakai)
        val inputHargaKemasan = binding.root.findViewById<EditText>(R.id.input_hargakemasan)
        val hasilEoq = binding.root.findViewById<TextView>(R.id.hasil_eoq)
        val status = binding.root.findViewById<TextView>(R.id.status)
        val hitungEoqButton = binding.root.findViewById<Button>(R.id.hitung_eoq)

        binding.settingApp.setOnClickListener {
            val intent = Intent(context, SettingActivity::class.java)
            startActivity(intent)
        }

        inputTanggal.setOnClickListener {
            showDatePicker(inputTanggal)
        }

        mDbRef = FirebaseDatabase.getInstance().reference.child("hasil_perhitungan")
        hitungEoqButton.setOnClickListener {
            val beratBahan = inputBahanBaku.text.toString().toFloatOrNull()
            val jumlahKemasan = inputKemasanTerpakai.text.toString().toIntOrNull()
            val hargaKemasan = inputHargaKemasan.text.toString().toIntOrNull()
            val tanggal = inputTanggal.text.toString()

            if (beratBahan != null && jumlahKemasan != null && hargaKemasan != null && tanggal.isNotEmpty()) {
                val hasilPerhitungan = eoqCalculatorAdapter.calculateEOQ(beratBahan, jumlahKemasan, hargaKemasan)
                hasilEoq.text = hasilPerhitungan.toString()

                if (hasilPerhitungan == 29.0) {
                    status.text = "Perhitungan EOQ Optimal"
                } else if (hasilPerhitungan < 29.0) {
                    status.text = "Perhitungan EOQ Kurang Optimal"
                } else {
                    status.text = "Perhitungan EOQ Berlebih"
                }
            } else {
                status.text = "Semua input harus diisi"
                Toast.makeText(context, "Semua Inputan Harus Terisi", Toast.LENGTH_SHORT).show()
            }


        }




        return binding.root
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = formatDate(selectedYear, selectedMonth, selectedDay)
                editText.setText(formattedDate)
            },
            year, month, day
        )

        datePickerDialog.show()
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        return dateFormat.format(calendar.time)
    }




}




