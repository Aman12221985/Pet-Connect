package com.example.petconnectt

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper

import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)




        Handler(Looper.getMainLooper()).postDelayed({
            // Redirect to LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 3000) // 2000 ms = 2 seconds
    }
}
