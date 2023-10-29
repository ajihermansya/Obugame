package com.rumahproduksi.obugame.page_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.rumahproduksi.obugame.R
import com.rumahproduksi.obugame.databinding.ActivityInventoriBinding
import com.rumahproduksi.obugame.databinding.ActivitySettingBinding

class InventoriActivity : AppCompatActivity() {
    lateinit var binding : ActivityInventoriBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.hide()
        }
    }
}