package com.rumahproduksi.obugame.page_activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rumahproduksi.obugame.adapter.DetailInventoryAdapter
import com.rumahproduksi.obugame.adapter.InventoryAdapter
import com.rumahproduksi.obugame.adapter.dataclass_model.BahanBaku
import com.rumahproduksi.obugame.adapter.dataclass_model.InventoriModel
import com.rumahproduksi.obugame.databinding.ActivityInventoriBinding
import java.text.SimpleDateFormat
import java.util.Date

class InventoriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInventoriBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var list: ArrayList<BahanBaku>
    private lateinit var lists: ArrayList<InventoriModel>
    private lateinit var mDbRef: DatabaseReference
    private lateinit var mDbRefs: DatabaseReference
    private lateinit var adapters: InventoryAdapter
    private var activeID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.hide()
        }

        activeID = intent.getStringExtra("id").toString()
        list = ArrayList()
        lists = ArrayList()
        database = FirebaseDatabase.getInstance()



        binding.progressBar.visibility = View.VISIBLE
        val adapter = DetailInventoryAdapter(this, list)
        mDbRef = database.reference.child("bahan_baku")
        mDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (snapshot1 in snapshot.children) {
                    val data = snapshot1.getValue(BahanBaku::class.java)
                    if (data?.id == activeID) {
                        list.add(data!!)
                    }
                }
                adapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@InventoriActivity, "Terjadi kesalahan pada database", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        })

        binding.recyclerView.adapter = adapter


        mDbRefs = database.reference.child("bahan_baku").child(activeID).child("inventori")
        mDbRefs.orderByChild("tanggal").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lists.clear()
                for (snapshot1 in snapshot.children) {
                    val data = snapshot1.getValue(InventoriModel::class.java)
                    if (data != null) {
                        lists.add(data)
                    }
                }
                adapters.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@InventoriActivity, "Terjadi kesalahan pada database", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        })
        adapters = InventoryAdapter(this, lists)
        binding.recyclerView.adapter = adapters






        binding.ProduksiBtn.setOnClickListener {
            if (binding.inputJumlahProduksi.text.isEmpty()) {
                Toast.makeText(this, "Inputkan angka yang sesuai", Toast.LENGTH_SHORT).show()
            } else {
                val jumlah = binding.inputJumlahProduksi.text.toString()
                val sdf = SimpleDateFormat("dd-MM-yyyy")
                val tanggal1 = Date()
                val tanggal = sdf.format(tanggal1)
                // Object data
                val produksi = InventoriModel(tanggal, jenisTindakan = "Produksi", jumlah, stok = null)
                // Mengirim data ke dalam Firebase sesuai dengan idnya
                val produksiReference = database.reference.child("bahan_baku")
                    .child(activeID)
                    .child("inventori")
                    .push()
                produksiReference.setValue(produksi)
                    .addOnSuccessListener {
                        binding.inputJumlahProduksi.text.clear()
                        Toast.makeText(this, "Data Tersimpan", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Data Tidak Tersimpan", Toast.LENGTH_SHORT).show()
                    }
            }
        }


    }
}
