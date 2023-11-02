package com.rumahproduksi.obugame.adapter.dataclass_model

import java.util.Date

data class InventoriModel(
    var tanggal: String? = "",
    var jenisTindakan: String? = "", //jumlahproduksi atau jumlah pembelian
    var jumlah: String? = "",
    var stok: String? = ""
)
