package com.rumahproduksi.obugame.adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rumahproduksi.obugame.R
import com.rumahproduksi.obugame.adapter.dataclass_model.BahanBaku
import com.rumahproduksi.obugame.databinding.CardBahanBakuBinding
import com.rumahproduksi.obugame.page_activity.InventoriActivity

class BahanBakuAdapter(private val context: Context, private val list: ArrayList<BahanBaku>)
    : RecyclerView.Adapter<BahanBakuAdapter.RiwayatViewHolder>(){
    private val handler = Handler()
    private val delay = 5000L  // 5 detik (dalam milidetik)
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

        val intent = Intent(context, InventoriActivity::class.java)
        holder.itemView.setOnClickListener {
            intent.putExtra("id", bahanBaku.id)
            context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            handler.postDelayed({
                deleteItemFromFirebase(bahanBaku)
            }, delay)

            true
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }


    private fun deleteItemFromFirebase(bahanBaku: BahanBaku) {
        val database = FirebaseDatabase.getInstance()
        val bahanBakuRef = database.reference.child("bahan_baku")

        val query = bahanBakuRef.orderByChild("jenispisang").equalTo(bahanBaku.jenispisang)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val keysToRemove = mutableListOf<String>()
                for (dataSnapshotChild in dataSnapshot.children) {
                    val key = dataSnapshotChild.key
                    if (key != null) {
                        keysToRemove.add(key)
                    }
                }

                if (keysToRemove.isNotEmpty()) {
                    for (key in keysToRemove) {
                        val itemRef = bahanBakuRef.child(key)
                        itemRef.removeValue()
                    }

                    // Tampilkan Toast saat data dihapus
                    showToast("Data dihapus: ${bahanBaku.jenispisang}")
                    removeItems(keysToRemove)
                } else {
                    showToast("Data tidak ditemukan")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error jika terjadi kesalahan
                showToast("Gagal menghapus data: ${databaseError.message}")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun removeItems(keys: List<String>) {
        list.removeAll { bahanBaku -> keys.contains(bahanBaku.id) }
        notifyDataSetChanged()
    }

}
