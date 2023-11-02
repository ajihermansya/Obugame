package com.rumahproduksi.obugame.page_activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rumahproduksi.obugame.R
import com.rumahproduksi.obugame.adapter.DetailAdapter
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
    private lateinit var mDbRefs: DatabaseReference
    private lateinit var adapters: InventoryAdapter
    private lateinit var mDbRef: DatabaseReference
    lateinit var adapter: DetailAdapter
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



        binding.progressBar.visibility = View.VISIBLE
        mDbRef = database.reference.child("bahan_baku")
        mDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                var lastStok: String? = null
                for (snapshot1 in snapshot.children) {
                    val data = snapshot1.getValue(BahanBaku::class.java)
                    if (data?.id == activeID) {
                        list.add(data!!)

                        binding.userName.text = data?.jenispisang

                        val inventoriRef = snapshot1.child("inventori")
                        val lastInventori = inventoriRef.children.lastOrNull()
                        lastStok = lastInventori?.child("stok")?.getValue(String::class.java)

                    }
                }
                binding.valueNilai.text = lastStok ?: "null"
                binding.progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@InventoriActivity, "Terjadi kesalahan pada database", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        })



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

                adapters.sortDataByDescending()
                binding.progressBar.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@InventoriActivity, "Terjadi kesalahan pada database", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        })
        adapters = InventoryAdapter(this, lists, activeID)
        binding.recyclerView.adapter = adapters





        binding.ProduksiBtn.setOnClickListener {
            if (binding.inputJumlahProduksi.text.isEmpty()) {
                Toast.makeText(this, "Inputkan angka yang sesuai", Toast.LENGTH_SHORT).show()
            } else {
                val jumlah = binding.inputJumlahProduksi.text.toString()
                val sdf = SimpleDateFormat("dd-MM-yyyy")
                val tanggal1 = Date()
                val tanggal = sdf.format(tanggal1)

                val inventoriRef = database.reference.child("bahan_baku")
                    .child(activeID)
                    .child("inventori")

                // Dapatkan data inventori terakhir
                inventoriRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val lastInventori = snapshot.children.firstOrNull()
                        val newId = if (lastInventori != null) {
                            // Jika ada data inventori sebelumnya, hitung ID yang baru
                            val lastInventoriId = lastInventori.key?.toInt()
                            (lastInventoriId ?: 0) + 1
                        } else {
                            1
                        }

                        val inventoriId = newId.toString()
                        var stokTerbaru = jumlah.toInt()

                        if (lastInventori != null) {
                            val lastStok = lastInventori.child("stok").getValue(String::class.java)
                            val lastStokInt = lastStok?.toIntOrNull() ?: 0  // Mengubah stok sebelumnya menjadi 0 jika null

                            stokTerbaru -= lastStokInt


                            val stokStr = if (stokTerbaru <= 0) {

                                "${Math.abs(stokTerbaru)}"
                            } else {

                                "-${Math.abs(stokTerbaru)}"
                            }


                            val produksi = InventoriModel(
                                inventoriId = inventoriId,
                                tanggal = tanggal,
                                jenisTindakan = "Produksi",
                                jumlah = jumlah,
                                stok = stokStr
                            )


                            val produksiReference = inventoriRef.child(inventoriId)
                            produksiReference.setValue(produksi)
                                .addOnSuccessListener {
                                    binding.inputJumlahProduksi.text.clear()
                                    Toast.makeText(this@InventoriActivity, "Data Tersimpan", Toast.LENGTH_SHORT).show()

                                    binding.root.findViewById<TextView>(R.id.jumlah_coloum)
                                        .setTextColor(ContextCompat.getColor(this@InventoriActivity, R.color.red))
                                    binding.root.findViewById<TextView>(R.id.jenis_coloum)
                                        .setTextColor(ContextCompat.getColor(this@InventoriActivity, R.color.red))
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this@InventoriActivity, "Data Tidak Tersimpan", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@InventoriActivity, "Gagal mengakses data inventori", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }



        binding.PembelianBtn.setOnClickListener {
            if (binding.inputJumlahProduksi.text.isEmpty()) {
                Toast.makeText(this, "Inputkan angka yang sesuai", Toast.LENGTH_SHORT).show()
            } else {
                val jumlah = binding.inputJumlahProduksi.text.toString()
                val sdf = SimpleDateFormat("dd-MM-yyyy")
                val tanggal1 = Date()
                val tanggal = sdf.format(tanggal1)

                val inventoriRef = database.reference.child("bahan_baku")
                    .child(activeID)
                    .child("inventori")

                // Dapatkan data inventori terakhir
                inventoriRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val lastInventori = snapshot.children.firstOrNull()
                        val newId = if (lastInventori != null) {
                            // Jika ada data inventori sebelumnya, hitung ID yang baru
                            val lastInventoriId = lastInventori.key?.toInt()
                            (lastInventoriId ?: 0) + 1
                        } else {
                            1
                        }

                        val inventoriId = newId.toString()
                        var stokTerbaru = jumlah.toInt()

                        // Jika ada data inventori sebelumnya, tambahkan jumlah dengan stok terakhir
                        if (lastInventori != null) {
                            val lastStok = lastInventori.child("stok").getValue(String::class.java)
                            stokTerbaru += lastStok?.toInt() ?: 0
                        }

                        // Membuat objek data inventori
                        val produksi = InventoriModel(
                            inventoriId = inventoriId,
                            tanggal = tanggal,
                            jenisTindakan = "Pembelian",
                            jumlah = jumlah,
                            stok = stokTerbaru.toString()
                        )

                        // Mengirim data ke Firebase dengan menggunakan ID inventori yang telah dihitung
                        val produksiReference = inventoriRef.child(inventoriId)
                        produksiReference.setValue(produksi)
                            .addOnSuccessListener {
                                binding.inputJumlahProduksi.text.clear()
                                Toast.makeText(this@InventoriActivity, "Data Tersimpan", Toast.LENGTH_SHORT).show()

                                // Mengubah warna teks menjadi hijau
                                binding.root.findViewById<TextView>(R.id.jumlah_coloum)
                                    .setTextColor(ContextCompat.getColor(this@InventoriActivity, R.color.green))
                                binding.root.findViewById<TextView>(R.id.jenis_coloum)
                                    .setTextColor(ContextCompat.getColor(this@InventoriActivity, R.color.green))
                            }
                            .addOnFailureListener {
                                Toast.makeText(this@InventoriActivity, "Data Tidak Tersimpan", Toast.LENGTH_SHORT).show()
                            }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@InventoriActivity, "Gagal mengakses data inventori", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }



        binding.clear.setOnClickListener {
            val inventoriRef = database.reference.child("bahan_baku")
                .child(activeID)
                .child("inventori")
            inventoriRef.removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this@InventoriActivity, "Semua data inventori telah dihapus", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this@InventoriActivity, "Gagal menghapus data inventori", Toast.LENGTH_SHORT).show()
                }
        }



        binding.iconBack.setOnClickListener {
            finish()
        }


    }

}
