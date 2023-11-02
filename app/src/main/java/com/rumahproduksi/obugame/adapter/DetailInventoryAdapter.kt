package com.rumahproduksi.obugame.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rumahproduksi.obugame.R
import com.rumahproduksi.obugame.adapter.dataclass_model.BahanBaku
import com.rumahproduksi.obugame.databinding.ActivityInventoriBinding
import com.rumahproduksi.obugame.page_activity.InventoriActivity

class DetailInventoryAdapter(private val context: Context, private val list: ArrayList<BahanBaku>)
    : RecyclerView.Adapter<DetailInventoryAdapter.RiwayatViewHolder>() {

    private val filteredList: ArrayList<BahanBaku> = ArrayList()

    inner class RiwayatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ActivityInventoriBinding = ActivityInventoriBinding.bind(view)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RiwayatViewHolder {
        return RiwayatViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_inventori, parent, false))
    }

    override fun onBindViewHolder(holder: DetailInventoryAdapter.RiwayatViewHolder, position: Int) {
        val bahanBaku = list[position]
        holder.binding.userName.text = bahanBaku.jenispisang

        holder.binding.iconBack.setOnClickListener {
            (context as InventoriActivity).finish()
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setFilteredData(data: List<BahanBaku>, activeID: String?) {
        filteredList.clear()
        activeID?.let { id ->
            filteredList.addAll(data.filter { it.id == id })
        }
        notifyDataSetChanged()
    }


}
