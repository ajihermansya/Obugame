package com.rumahproduksi.obugame.page_activity

import HistoryAdapter
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rumahproduksi.obugame.adapter.dataclass_model.CalculatorModel
import com.rumahproduksi.obugame.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var list: ArrayList<CalculatorModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.hide()
        }

        list = ArrayList() // Inisialisasi list
        database = FirebaseDatabase.getInstance()
        binding.progressBar.visibility = View.VISIBLE
        val adapter = HistoryAdapter(this, list)

        database.reference.child("hasil_perhitungan")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (snapshot1 in snapshot.children) {
                        val data = snapshot1.getValue(CalculatorModel::class.java)
                        list.add(data!!)
                    }
                    adapter.sortDataByDescending()
                    binding.recyclerView.adapter = adapter
                    binding.progressBar.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@HistoryActivity, "Error : ${error}", Toast.LENGTH_SHORT).show()
                    // Setelah data tampil (atau gagal), sembunyikan ProgressBar
                    binding.progressBar.visibility = View.GONE
                }
            })

        binding.floatingActionButton.setOnClickListener {
            val inventoriRef = database.reference.child("hasil_perhitungan")
            inventoriRef.removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this@HistoryActivity, "Semua data inventori telah dihapus", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this@HistoryActivity, "Gagal menghapus data inventori", Toast.LENGTH_SHORT).show()
                }
        }


        binding.iconBack.setOnClickListener {
            finish()
        }
    }
}
