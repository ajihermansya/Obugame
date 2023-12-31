package com.rumahproduksi.obugame.activity_fragmen

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.rumahproduksi.obugame.R
import com.rumahproduksi.obugame.adapter.BahanBakuAdapter
import com.rumahproduksi.obugame.adapter.dataclass_model.BahanBaku
import com.rumahproduksi.obugame.databinding.FragmentBahanBakuBinding

class BahanBakuFragment : Fragment() {
    lateinit var binding: FragmentBahanBakuBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var list: ArrayList<BahanBaku>
    private lateinit var storage : FirebaseStorage
    private var mStorageRef: StorageReference? = null
    private lateinit var mDbRef: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBahanBakuBinding.inflate(inflater, container, false)
        list = ArrayList()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        mStorageRef = FirebaseStorage.getInstance().reference

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
                    binding.recyclerView.adapter = adapter
                    binding.progressBar.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Error : ${error}", Toast.LENGTH_SHORT).show()
                    // Setelah data tampil (atau gagal), sembunyikan ProgressBar
                    binding.progressBar.visibility = View.GONE
                }
            })


        mDbRef = FirebaseDatabase.getInstance().reference.child("bahan_baku")
        binding.floatingActionButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val inflater = requireActivity().layoutInflater
            val dialogView = inflater.inflate(R.layout.cardview_popup, null)
            builder.setView(dialogView)
            val alertDialog = builder.create()
            alertDialog.show()

            val simpanData = dialogView.findViewById<Button>(R.id.simpan_data)

            simpanData.setOnClickListener {
                val inputBahanBaku = dialogView.findViewById<EditText>(R.id.input_bahanbaku_1).text.toString()
                tambahdata(inputBahanBaku)
                alertDialog.dismiss()
            }
        }

        return binding.root
    }

    private fun tambahdata(inputBahanBaku: String) {
        if (TextUtils.isEmpty(inputBahanBaku)) {
            Toast.makeText(context, "Isi semua kolom terlebih dahulu.", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // Menambahkan data ke Firebase Database
            val newBahanBakuRef = mDbRef.push()
            val newBahanBaku = BahanBaku(null, inputimage = null, inputBahanBaku) // ID diisi null di sini
            newBahanBakuRef.setValue(newBahanBaku)

            // Setel ID yang dihasilkan oleh push
            newBahanBaku.id = newBahanBakuRef.key
            mDbRef.child(newBahanBaku.id!!).setValue(newBahanBaku)

            Toast.makeText(context, "Data berhasil ditambahkan ke Firebase", Toast.LENGTH_SHORT).show()
        } catch (e: NumberFormatException) {
            Toast.makeText(context, "Masukkan berat bahan tidak valid. Harap masukkan angka yang benar.", Toast.LENGTH_SHORT).show()
        }
    }



}