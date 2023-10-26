package com.rumahproduksi.obugame.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rumahproduksi.obugame.R
import com.rumahproduksi.obugame.adapter.dataclass_model.CalculatorModel
import com.rumahproduksi.obugame.databinding.CardHistoryBinding

class HistoryAdapter(private val context: Context, private val list: ArrayList<CalculatorModel>)
    : RecyclerView.Adapter<HistoryAdapter.RiwayatViewHolder>(){

    inner class RiwayatViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val binding : CardHistoryBinding = CardHistoryBinding.bind(view)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RiwayatViewHolder {
       return RiwayatViewHolder(LayoutInflater.from(parent.context)
           .inflate(R.layout.card_history, parent, false))
    }

    override fun onBindViewHolder(holder: HistoryAdapter.RiwayatViewHolder, position: Int) {
        val calculatorModel = list[position]
        holder.binding.hasilEoq.text = calculatorModel.hasileoq
        holder.binding.jumlahBahanBaku.text = calculatorModel.beratBahan
        holder.binding.jumlahKemasanTerpakai.text = calculatorModel.jumlahKemasan
        holder.binding.tanggal.text = calculatorModel.tanggal

    }

    override fun getItemCount(): Int {
     return list.size
    }
}