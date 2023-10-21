package com.rumahproduksi.obugame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        var handler = Handler()
        handler.postDelayed({
            var intent = Intent(this@SplashScreenActivity, HomePageActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}