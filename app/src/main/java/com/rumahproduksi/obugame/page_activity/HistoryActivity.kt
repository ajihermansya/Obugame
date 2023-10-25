package com.rumahproduksi.obugame.page_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rumahproduksi.obugame.databinding.ActivityHistoryBinding
import com.rumahproduksi.obugame.databinding.ActivitySettingBinding

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}