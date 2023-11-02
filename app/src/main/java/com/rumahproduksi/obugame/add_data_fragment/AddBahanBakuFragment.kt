package com.rumahproduksi.obugame.add_data_fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rumahproduksi.obugame.adapter.dataclass_model.BahanBaku
import com.rumahproduksi.obugame.databinding.FragmentAddBahanBakuBinding

class AddBahanBakuFragment : Fragment() {
    lateinit var binding: FragmentAddBahanBakuBinding
    private lateinit var mDbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBahanBakuBinding.inflate(inflater, container, false)
        mDbRef = FirebaseDatabase.getInstance().reference.child("bahan_baku")

        binding.buttonSave.setOnClickListener {
            val jenispisang = binding.jenisPisang.text.toString()
            tambahdata(jenispisang)
        }

        return binding.root
    }

    private fun tambahdata(jenispisang: String) {
        if (TextUtils.isEmpty(jenispisang)) {
            Toast.makeText(context, "Isi semua kolom terlebih dahulu.", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val newBahanBaku = BahanBaku(null, inputimage = null, jenispisang) // ID diisi null di sini
            val newBahanBakuRef = mDbRef.push()
            newBahanBakuRef.setValue(newBahanBaku)
            newBahanBaku.id = newBahanBakuRef.key
            mDbRef.child(newBahanBaku.id!!).setValue(newBahanBaku)

            Toast.makeText(context, "Data berhasil ditambahkan ke Firebase", Toast.LENGTH_SHORT).show()
        } catch (e: NumberFormatException) {
            Toast.makeText(context, "Masukkan berat bahan tidak valid. Harap masukkan angka yang benar.", Toast.LENGTH_SHORT).show()
        }
    }

}
