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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalculatorBinding.inflate(inflater, container, false)

        val inputTanggal = binding.root.findViewById<EditText>(R.id.tanggal_input)
        val inputBahanBaku = binding.root.findViewById<EditText>(R.id.input_bahanbaku)
        val inputKemasanTerpakai = binding.root.findViewById<EditText>(R.id.input_kemasanterpakai)
        val inputHargaKemasan = binding.root.findViewById<EditText>(R.id.input_hargakemasan)
        val hasilEoq = binding.root.findViewById<EditText>(R.id.hasil_eoq)
        val hitungEoqButton = binding.root.findViewById<Button>(R.id.hitung_eoq)

        binding.settingApp.setOnClickListener {
            val intent = Intent(context, SettingActivity::class.java)
            startActivity(intent)
        }

        inputTanggal.setOnClickListener {
            showDatePicker(inputTanggal)
        }

        hitungEoqButton.setOnClickListener {
            val beratBahan = inputBahanBaku.text.toString().toDoubleOrNull() ?: 0.0
            val jumlahKemasan = inputKemasanTerpakai.text.toString().toDoubleOrNull() ?: 0.0
            val hargaKemasan = inputHargaKemasan.text.toString().toDoubleOrNull() ?: 0.0
            val hasilPerhitungan = eoqCalculatorAdapter.calculateEOQ(beratBahan, jumlahKemasan, hargaKemasan)
            hasilEoq.setText(hasilPerhitungan.toString())
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
