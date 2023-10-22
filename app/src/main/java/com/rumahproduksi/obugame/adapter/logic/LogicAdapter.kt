package com.rumahproduksi.obugame.adapter.logic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rumahproduksi.obugame.databinding.CardCalculatorBinding

class LogicAdapter(private val dataList: List<CardData>) :
    RecyclerView.Adapter<LogicAdapter.CardViewHolder>() {

    inner class CardViewHolder(val binding: CardCalculatorBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = CardCalculatorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val data = dataList[position]
        holder.binding.inputBahanbaku.setText(data.bahanBaku.toString())
        holder.binding.inputKemasanterpakai.setText(data.kemasanTerpakai.toString())
        holder.binding.inputHargakemasan.setText(data.hargaKemasan.toString())



        // Set click listeners and other logic for buttons here
        holder.binding.hitungEoq.setOnClickListener {
            // Handle the logic for the "Hitung" button
        }

        holder.binding.buttonRiwayat.setOnClickListener {
            // Handle the logic for the "Riwayat" button
        }

        holder.binding.buttonSave.setOnClickListener {
            // Handle the logic for the "Save" button
        }


    }

    override fun getItemCount(): Int = dataList.size
}
