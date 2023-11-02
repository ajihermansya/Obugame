package com.rumahproduksi.obugame.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rumahproduksi.obugame.R
import com.rumahproduksi.obugame.adapter.dataclass_model.InventoriModel
import com.rumahproduksi.obugame.databinding.CardTableBinding


class InventoryBiayaAdapter(private val context: Context?, private val list: ArrayList<InventoriModel>, private val activeID: String )
    :RecyclerView.Adapter<InventoryBiayaAdapter.DetailViewHolder>() {

    private val clickThreshold = 3
    private var clickCount = 0
    private var lastClickedPosition = -1

    inner class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: CardTableBinding = CardTableBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        return DetailViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.card_table, parent, false))
    }


    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
       val inventoriModel = list[position]
        holder.binding.tanggalColoum.text = inventoriModel.tanggal
        holder.binding.jenisColoum.text = inventoriModel.jenisTindakan
        holder.binding.jumlahColoum.text = inventoriModel.jumlah
        holder.binding.stokColoum.text = inventoriModel.stok


        val iconPlus = "+"
        val iconMinus = "-"

        holder.binding.jumlahColoum.text = when (inventoriModel.jenisTindakan) {
            "Produksi" -> {
                holder.binding.jumlahColoum.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
                // Gunakan ikon minus jika jumlah negatif
                if (inventoriModel.jumlah?.toInt()!! < 0) {
                    "$iconMinus ${inventoriModel.jumlah}"
                } else {
                    "$iconMinus ${inventoriModel.jumlah}"
                }
            }
            "Pembelian" -> {
                holder.binding.jumlahColoum.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
                // Gunakan ikon plus
                "$iconPlus ${inventoriModel.jumlah}"
            }
            else -> {
                holder.binding.jumlahColoum.setBackgroundResource(R.drawable.brown_border)
                inventoriModel.jumlah
            }
        }



        holder.itemView.setOnClickListener {
            onItemClicked(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun onItemClicked(position: Int) {
        if (position == lastClickedPosition) {
            clickCount++

        } else {
            clickCount = 1
            Toast.makeText(context, "Tekan 2 Kali Lagi Untuk Menghapus Data!", Toast.LENGTH_SHORT).show()
        }

        if (clickCount == clickThreshold) {
            deleteItem(position)
            clickCount = 0
        }

        Toast.makeText(context, "Tekan 1 Kali Lagi Untuk Menghapus Data!", Toast.LENGTH_SHORT).show()
        lastClickedPosition = position


    }

    private fun deleteItem(position: Int) {
        if (position < list.size) {
            val itemId = list[position].inventoriId
            val databaseReference = FirebaseDatabase.getInstance().getReference("biaya_lain").child(activeID).child("inventori")

            // Hapus item berdasarkan ID
            databaseReference.orderByChild("inventoriId").equalTo(itemId).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (childSnapshot in snapshot.children) {
                        childSnapshot.ref.removeValue()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Gagal menghapus data dari Firebase", Toast.LENGTH_SHORT).show()
                }
            })


        }
    }

    fun sortDataByDescending() {
        list.sortByDescending { it.inventoriId?.toIntOrNull() ?: 0  }
        notifyDataSetChanged()
    }


}




