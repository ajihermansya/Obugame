package com.rumahproduksi.obugame.fragment_setting

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rumahproduksi.obugame.databinding.FragmentAddBahanBakuBinding
import com.rumahproduksi.obugame.model.BahanBaku

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
            val beratBahan = binding.inputBahanbaku.text.toString()
           tambahdata(jenispisang, beratBahan)
        }

        return binding.root
    }

    private fun tambahdata(jenispisang : String, beratBahan : String){
        if (TextUtils.isEmpty(jenispisang) || TextUtils.isEmpty(beratBahan)) {
            Toast.makeText(context, "Isi semua kolom terlebih dahulu.", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            //Menambahkan data ke Firebase
            val newBahanBakuRef = mDbRef.push()
            newBahanBakuRef.setValue(BahanBaku(jenispisang, beratBahan))
            Toast.makeText(context, "Data berhasil ditambahkan ke Firebase", Toast.LENGTH_SHORT).show()
        } catch (e: NumberFormatException) {
            Toast.makeText(context, "Masukkan berat bahan tidak valid. Harap masukkan angka yang benar.", Toast.LENGTH_SHORT).show()
        }
    }
}
