package com.rumahproduksi.obugame
import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.rumahproduksi.obugame.adapter.logic.adapter_fragment.DetailAdapter
import com.rumahproduksi.obugame.databinding.ActivityHomePageBinding
import com.rumahproduksi.obugame.fragmenactivity.BahanBakuFragment
import com.rumahproduksi.obugame.fragmenactivity.BahanLainnyaFragment
import com.rumahproduksi.obugame.fragmenactivity.BiayaLainFragment
import com.rumahproduksi.obugame.menu_fragment.NotedFragment
import com.rumahproduksi.obugame.menu_fragment.calculatorFragment

class HomePageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.hide()
        }

        bottomNavigationView = binding.BottomNavMenu
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.calculet -> {
                    replaceFragment(calculatorFragment())
                    val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
                    startActivity(intent, options.toBundle())
                    true
                }
                R.id.noted -> {
                    replaceFragment(NotedFragment())
                    val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left)
                    startActivity(intent, options.toBundle())
                    true
                }

                R.id.profile -> {
                    Toast.makeText(this, "Ini adalah halaman profil.", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> {
                    Toast.makeText(this, "Item yang dipilih tidak dikenali.", Toast.LENGTH_SHORT).show()
                    false
                }

            }

        }
        replaceFragment(calculatorFragment())


    }

    private fun replaceFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }

}



