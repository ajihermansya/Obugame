package com.rumahproduksi.obugame.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rumahproduksi.obugame.R
import com.rumahproduksi.obugame.adapter.dataclass_model.BahanBaku
import com.rumahproduksi.obugame.adapter.dataclass_model.CalculatorModel
import com.rumahproduksi.obugame.databinding.CardBahanBakuBinding
import com.rumahproduksi.obugame.databinding.CardHistoryBinding

class BahanBakuAdapter(private val context: Context, private val list: ArrayList<BahanBaku>)
    : RecyclerView.Adapter<BahanBakuAdapter.RiwayatViewHolder>(){

    inner class RiwayatViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val binding : CardBahanBakuBinding = CardBahanBakuBinding.bind(view)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RiwayatViewHolder {
       return RiwayatViewHolder(LayoutInflater.from(parent.context)
           .inflate(R.layout.card_bahan_baku, parent, false))
    }

    override fun onBindViewHolder(holder: BahanBakuAdapter.RiwayatViewHolder, position: Int) {
        val bahanBaku = list[position]
        holder.binding.userName.text = bahanBaku.jenispisang

    }

    override fun getItemCount(): Int {
     return list.size
    }

//    fun sortDataByDescending() {
//        list.sortByDescending { it.newId }
//        notifyDataSetChanged()
//    }
}