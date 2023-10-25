package com.rumahproduksi.obugame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.rumahproduksi.obugame.adapter.DetailAdapter
import com.rumahproduksi.obugame.databinding.ActivitySettingBinding
import com.rumahproduksi.obugame.fragment_setting.AddBahanBakuFragment
import com.rumahproduksi.obugame.fragment_setting.AddBahanLainnyaFragment
import com.rumahproduksi.obugame.fragment_setting.AddBiayaLainnyaFragment

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragments = mutableListOf<Fragment>(
            AddBahanBakuFragment(),
            AddBahanLainnyaFragment(),
            AddBiayaLainnyaFragment()
        )
        val titleFragments = mutableListOf(
            getString(R.string.bahanbaku), getString(R.string.bahanlainnya), getString(R.string.biayalainnya)
        )
        val adapter = DetailAdapter(this, fragments)
        binding.viewpager.adapter = adapter
        TabLayoutMediator(binding.tab,binding.viewpager) { tab, posisi ->
            tab.text = titleFragments[posisi]
        }.attach()
    }
}