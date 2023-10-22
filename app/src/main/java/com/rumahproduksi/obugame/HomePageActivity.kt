package com.rumahproduksi.obugame
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.rumahproduksi.obugame.adapter.adapter_fragment.DetailAdapter
import com.rumahproduksi.obugame.databinding.ActivityHomePageBinding
import com.rumahproduksi.obugame.fragmenactivity.BahanBakuFragment
import com.rumahproduksi.obugame.fragmenactivity.BahanLainnyaFragment
import com.rumahproduksi.obugame.fragmenactivity.BiayaLainFragment

class HomePageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.hide()
        }

        val fragments = mutableListOf<Fragment>(
            BahanBakuFragment(),
            BahanLainnyaFragment(),
            BiayaLainFragment()
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