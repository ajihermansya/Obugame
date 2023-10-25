package com.rumahproduksi.obugame.menu_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.rumahproduksi.obugame.R
import com.rumahproduksi.obugame.adapter.DetailAdapter
import com.rumahproduksi.obugame.databinding.FragmentNotedBinding
import com.rumahproduksi.obugame.fragmenactivity.BahanBakuFragment
import com.rumahproduksi.obugame.fragmenactivity.BahanLainnyaFragment
import com.rumahproduksi.obugame.fragmenactivity.BiayaLainFragment


class NotedFragment : Fragment() {
    lateinit var binding: FragmentNotedBinding

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
        val adapter = DetailAdapter(this.requireActivity(), fragments)
        binding.viewpager.adapter = adapter
        TabLayoutMediator(binding.tab,binding.viewpager) { tab, posisi ->
            tab.text = titleFragments[posisi]
        }.attach()


        return binding.root
    }

}