package com.rumahproduksi.obugame.adapter.logic
import kotlin.math.round

class EOQCalculatorAdapter {
    fun calculateEOQ(
        beratBahan: Double,
        jumlahKemasan: Double,
        hargaKemasan: Double
    ): Double {
        return round(beratBahan * jumlahKemasan * hargaKemasan)
    }
}