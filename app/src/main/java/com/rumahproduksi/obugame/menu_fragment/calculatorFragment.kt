package com.rumahproduksi.obugame.menu_fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rumahproduksi.obugame.R
import com.rumahproduksi.obugame.adapter.dataclass_model.CalculatorModel
import com.rumahproduksi.obugame.adapter.logic.EOQCalculatorAdapter
import com.rumahproduksi.obugame.databinding.FragmentCalculatorBinding
import com.rumahproduksi.obugame.page_activity.HistoryActivity
import java.text.SimpleDateFormat
import java.util.Date

class calculatorFragment : Fragment() {
    lateinit var binding: FragmentCalculatorBinding
    private val eoqCalculatorAdapter = EOQCalculatorAdapter()
    private lateinit var mDbRef: DatabaseReference
    private lateinit var status: TextView
    private lateinit var hasilEoq: TextView
    private var dataSudahDisimpan = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        binding = FragmentCalculatorBinding.inflate(inflater, container, false)

        val inputBahanBaku = binding.root.findViewById<EditText>(R.id.input_bahanbaku)
        val inputKemasanTerpakai = binding.root.findViewById<EditText>(R.id.input_kemasanterpakai)
        val inputHargaKemasan = binding.root.findViewById<EditText>(R.id.input_hargakemasan)
        val bersihkandata = binding.root.findViewById<ImageView>(R.id.clear)
        val riwayat = binding.root.findViewById<Button>(R.id.button_riwayat)
        hasilEoq = binding.root.findViewById(R.id.hasil_eoq)
        status = binding.root.findViewById(R.id.status)
        val hitungEoqButton = binding.root.findViewById<Button>(R.id.hitung_eoq)

        riwayat.setOnClickListener {
            val intent = Intent(context, HistoryActivity::class.java)
            startActivity(intent)
        }


        bersihkandata.setOnClickListener { //finish()
            inputBahanBaku.setText("")
            inputKemasanTerpakai.setText("")
            inputHargaKemasan.setText("")
            hasilEoq.text = ""
            status.text = ""
        }



        mDbRef = FirebaseDatabase.getInstance().reference.child("hasil_perhitungan")
        hitungEoqButton.setOnClickListener {
            val beratBahan = inputBahanBaku.text.toString().toFloatOrNull()
            val jumlahKemasan = inputKemasanTerpakai.text.toString().toIntOrNull()
            val hargaKemasan = inputHargaKemasan.text.toString().toIntOrNull()
            tambahHasil(beratBahan, jumlahKemasan, hargaKemasan )
        }

        return binding.root
    }


    private fun tambahHasil(beratBahan: Float?, jumlahKemasan: Int?, hargaKemasan: Int?) {
        if (beratBahan != null && jumlahKemasan != null && hargaKemasan != null) {
            val hasileoq = eoqCalculatorAdapter.calculateEOQ(beratBahan, jumlahKemasan, hargaKemasan)
            hasilEoq.text = hasileoq.toString()

            val statusText = if (hasileoq == 29.0) {
                "Persedian Bahan Baku Sudah Optimal"
            } else if (hasileoq < 29.0) {
                "Persedian Bahan Baku Kurang Optimal"
            } else {
                "Persedian Bahan Baku Berlebih"
            }
            status.text = statusText

            if (!dataSudahDisimpan) {
                // Menambahkan data ke Firebase
                val firebaseDatabase = FirebaseDatabase.getInstance()
                val mDbRef = firebaseDatabase.getReference("hasil_perhitungan")

                // Ambil data ID terakhir dari Firebase
                mDbRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val newId = if (snapshot.hasChildren()) {
                            val lastId = snapshot.children.first().key?.toInt() ?: 0
                            lastId + 1
                        } else {
                            1 // Jika tidak ada data sebelumnya, gunakan ID awal
                        }

                        val sdf = SimpleDateFormat("dd-MM-yyyy")
                        val tanggal1 = Date()
                        val tanggal = sdf.format(tanggal1)

                        val newCalculatorModel = CalculatorModel(
                            newId,
                            beratBahan.toString(),
                            jumlahKemasan.toString(),
                            hargaKemasan.toString(),
                            hasileoq.toString(),
                            tanggal = tanggal
                        )

                        mDbRef.child(newId.toString()).setValue(newCalculatorModel)

                        Toast.makeText(context, "Data berhasil ditambahkan ke Riwayat", Toast.LENGTH_SHORT).show()
                        dataSudahDisimpan = true

                        // Setelah 5 detik, atur kembali dataSudahDisimpan ke false
                        Handler().postDelayed({
                            dataSudahDisimpan = false
                        }, 5000) // 5000 milliseconds = 5 seconds
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(context, "Data telah disimpan dalam 5 detik terakhir", Toast.LENGTH_SHORT).show()
            }
        } else {
            status.text = "Semua input harus diisi"
            Toast.makeText(context, "Semua Inputan Harus Terisi", Toast.LENGTH_SHORT).show()
        }
    }

}


