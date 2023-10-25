package com.rumahproduksi.obugame.adapter.logic
import kotlin.math.round

import kotlin.math.sqrt

class EOQCalculatorAdapter {
    fun calculateEOQ(
        beratBahan: Float,
        jumlahKemasan: Int,
        hargaKemasan: Int
    ): Double {
        val D = 8794.0
        val S = beratBahan
        val H = jumlahKemasan * hargaKemasan

        val EOQ = sqrt((2 * D * S) / H)
        return round(EOQ)
    }


}
