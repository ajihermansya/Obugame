package com.rumahproduksi.obugame.menu_fragment
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.rumahproduksi.obugame.R
import com.rumahproduksi.obugame.databinding.FragmentCalculatorBinding
import java.util.Calendar


class calculatorFragment : Fragment() {
    lateinit var binding: FragmentCalculatorBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCalculatorBinding.inflate(inflater, container, false)


        val inputTanggal = binding.root.findViewById<EditText>(R.id.tanggal_input)
        inputTanggal.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Mengonversi bulan ke format 2 digit (01, 02, 03, dll.)
                    val formattedMonth = if (selectedMonth + 1 < 10) "0${selectedMonth + 1}" else (selectedMonth + 1).toString()
                    // Menggabungkan tanggal, bulan, dan tahun dalam format "tanggal bulan tahun"
                    val selectedDate = "$selectedDay-$formattedMonth-$selectedYear"
                    inputTanggal.setText(selectedDate)
                },
                year, month, day
            )

            datePickerDialog.show()
        }




        return binding.root
    }


}