import android.content.Context
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
import com.rumahproduksi.obugame.adapter.dataclass_model.CalculatorModel
import com.rumahproduksi.obugame.databinding.CardHistoryBinding

class HistoryAdapter(private val context: Context, private val list: ArrayList<CalculatorModel>)
    : RecyclerView.Adapter<HistoryAdapter.RiwayatViewHolder>() {

    private val clickThreshold = 3
    private var clickCount = 0
    private var lastClickedPosition = -1

    inner class RiwayatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: CardHistoryBinding = CardHistoryBinding.bind(view)
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
        } else {
            Toast.makeText(context, "Tekan 1 Kali Lagi Untuk Menghapus Data!", Toast.LENGTH_SHORT).show()
            lastClickedPosition = position
        }
    }

    private fun deleteItem(position: Int) {
        if (position < list.size) {
            val itemId = list[position].newId
            val databaseReference = FirebaseDatabase.getInstance().reference.child("hasil_perhitungan")

            // Hapus item berdasarkan ID dari Firebase
            databaseReference.orderByChild("newId").equalTo(itemId.toDouble()).addListenerForSingleValueEvent(object :
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

            // Hapus item dari daftar setelah menghapus dari Firebase
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun sortDataByDescending() {
        list.sortByDescending { it.newId }
        notifyDataSetChanged()
    }
}
