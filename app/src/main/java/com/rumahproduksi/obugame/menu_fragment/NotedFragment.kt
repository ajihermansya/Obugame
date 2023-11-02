package com.rumahproduksi.obugame.menu_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.rumahproduksi.obugame.R
import com.rumahproduksi.obugame.activity_fragmen.BahanBakuFragment
import com.rumahproduksi.obugame.activity_fragmen.BahanLainnyaFragment
import com.rumahproduksi.obugame.activity_fragmen.BiayaLainFragment
import com.rumahproduksi.obugame.adapter.DetailAdapter
import com.rumahproduksi.obugame.databinding.FragmentNotedBinding

class NotedFragment : Fragment() {
    lateinit var binding: FragmentNotedBinding
    lateinit var adapter: DetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotedBinding.inflate(inflater, container, false)

        val fragments = mutableListOf<Fragment>(
            BahanBakuFragment(),
            BahanLainnyaFragment(),
            BiayaLainFragment()
        )

        val titleFragments = mutableListOf(
            getString(R.string.bahanbaku), getString(R.string.bahanlainnya), getString(R.string.biayalainnya)
        )

        // Inisialisasi adapter dengan data fragment
        adapter = DetailAdapter(requireContext() as FragmentActivity, fragments)
        binding.viewpager.adapter = adapter
        TabLayoutMediator(binding.tab, binding.viewpager) { tab, posisi ->
            tab.text = titleFragments[posisi]
        }.attach()

//        binding.settingApp.setOnClickListener {
//            val intent = Intent(requireContext(), SettingActivity::class.java)
//            startActivity(intent)
//        }

        return binding.root
    }
}
