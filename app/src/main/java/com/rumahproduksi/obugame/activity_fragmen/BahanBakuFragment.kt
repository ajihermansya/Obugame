package com.rumahproduksi.obugame.activity_fragmen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rumahproduksi.obugame.adapter.BahanBakuAdapter
import com.rumahproduksi.obugame.adapter.dataclass_model.BahanBaku
import com.rumahproduksi.obugame.databinding.FragmentBahanBakuBinding


class BahanBakuFragment : Fragment() {
    lateinit var binding: FragmentBahanBakuBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var list: ArrayList<BahanBaku>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBahanBakuBinding.inflate(inflater, container, false)


        list = ArrayList() // Inisialisasi list
        database = FirebaseDatabase.getInstance()
        binding.progressBar.visibility = View.VISIBLE
        val adapter = context?.let { BahanBakuAdapter(it, list) }

        database.reference.child("bahan_baku")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (snapshot1 in snapshot.children) {
                        val data = snapshot1.getValue(BahanBaku::class.java)
                        list.add(data!!)
                    }
//                    if (adapter != null) {
//                        adapter.sortDataByDescending()
//                    }
                    binding.recyclerView.adapter = adapter
                    binding.progressBar.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Error : ${error}", Toast.LENGTH_SHORT).show()
                    // Setelah data tampil (atau gagal), sembunyikan ProgressBar
                    binding.progressBar.visibility = View.GONE
                }
            })

        return binding.root
    }

}