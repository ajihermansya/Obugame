package com.rumahproduksi.obugame.adapter.dataclass_model

data class InventoriModel(
    var inventoriId:String? = "",
    var tanggal: String? = "",
    var jenisTindakan: String? = "", //jumlahproduksi atau jumlah pembelian
    var jumlah: String? = "",
    var stok: String? = ""
)
