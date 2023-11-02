package com.rumahproduksi.obugame.page_activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rumahproduksi.obugame.R
import com.rumahproduksi.obugame.databinding.ActivityHomePageBinding
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
                    true
                }
                R.id.noted -> {
                    replaceFragment(NotedFragment())
                    true
                }
                R.id.profile -> {
                    val alertDialogBuilder = AlertDialog.Builder(this)
                    alertDialogBuilder.setTitle("Halaman Profil")
                    alertDialogBuilder.setMessage("Maaf Halaman Ini Dalam Sedang Perbaikan")
                    alertDialogBuilder.setPositiveButton("Tutup") { dialog, _ ->
                        dialog.dismiss()
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
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



