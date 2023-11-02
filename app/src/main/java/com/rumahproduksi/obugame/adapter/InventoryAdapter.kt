package com.rumahproduksi.obugame.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rumahproduksi.obugame.R
import com.rumahproduksi.obugame.adapter.dataclass_model.InventoriModel
import com.rumahproduksi.obugame.databinding.CardDetailActivityBinding


class InventoryAdapter(private val context: Context, private val list: ArrayList<InventoriModel>)
    :RecyclerView.Adapter<InventoryAdapter.DetailViewHolder>() {

    inner class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: CardDetailActivityBinding = CardDetailActivityBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        return DetailViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.card_detail_activity, parent, false))
    }



    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
       val inventoriModel = list[position]
        holder.binding.tanggalColoum.text = inventoriModel.tanggal
        holder.binding.jenisColoum.text = inventoriModel.jenisTindakan
        holder.binding.jumlahColoum.text = inventoriModel.jumlah
    }

    override fun getItemCount(): Int {
        return list.size
    }

}




