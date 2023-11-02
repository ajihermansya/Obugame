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
import com.rumahproduksi.obugame.adapter.dataclass_model.BahanLainnyaModel
import com.rumahproduksi.obugame.databinding.CardBahanBakuBinding
import com.rumahproduksi.obugame.page_activity.inventory_activity.BahanLainnyaActivity

class BahanLainnyaAdapter(private val context: Context, private val list: ArrayList<BahanLainnyaModel>)
    : RecyclerView.Adapter<BahanLainnyaAdapter.RiwayatViewHolder>(){
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

    override fun onBindViewHolder(holder: BahanLainnyaAdapter.RiwayatViewHolder, position: Int) {
        val bahanLainnyaModel = list[position]
        holder.binding.userName.text = bahanLainnyaModel.namabahan

        val intent = Intent(context, BahanLainnyaActivity::class.java)
        holder.itemView.setOnClickListener {
            intent.putExtra("id", bahanLainnyaModel.id)
            //Toast.makeText(context, "ini id bahan lainnya : ${bahanLainnyaModel.id}", Toast.LENGTH_SHORT).show()
            context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            handler.postDelayed({
                deleteItemFromFirebase(bahanLainnyaModel)
            }, delay)

            true
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }


    private fun deleteItemFromFirebase(bahanLainnyaModel: BahanLainnyaModel) {
        val database = FirebaseDatabase.getInstance()
        val bahanLainnyaRef = database.reference.child("bahan_lainnya")

        val query = bahanLainnyaRef.orderByChild("namabahan").equalTo(bahanLainnyaModel.namabahan)
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
                        val itemRef = bahanLainnyaRef.child(key)
                        itemRef.removeValue()
                    }

                    // Tampilkan Toast saat data dihapus
                    showToast("Data berhasil dihapus: ${bahanLainnyaModel.namabahan}")
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
        list.removeAll { bahanLainnyaModel -> keys.contains(bahanLainnyaModel.id) }
        notifyDataSetChanged()
    }

}
