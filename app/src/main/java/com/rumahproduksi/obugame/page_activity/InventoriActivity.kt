package com.rumahproduksi.obugame.page_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rumahproduksi.obugame.adapter.BahanBakuAdapter
import com.rumahproduksi.obugame.adapter.DetailInventoryAdapter
import com.rumahproduksi.obugame.adapter.dataclass_model.BahanBaku
import com.rumahproduksi.obugame.databinding.ActivityInventoriBinding

class InventoriActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInventoriBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var list: ArrayList<BahanBaku>
    private lateinit var mStorageRef: StorageReference
    private lateinit var mDbRef: DatabaseReference
    private var activeID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

        activeID = intent.getStringExtra("id").toString()

        list = ArrayList()
        database = FirebaseDatabase.getInstance()
        mStorageRef = FirebaseStorage.getInstance().reference
        mDbRef = database.reference.child("bahan_baku")

        binding.progressBar.visibility = View.VISIBLE
        val adapter = DetailInventoryAdapter(this, list)

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
                Toast.makeText(this@InventoriActivity, "Error : ${error}", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        })

        // Atur RecyclerView untuk menggunakan adapter
        binding.recyclerView.adapter = adapter
    }
}
