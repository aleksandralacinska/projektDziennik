package com.example.dziennikmvvm.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.dziennikmvvm.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Przekierowanie po 3 sekundach do ekranu Journal
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, JournalActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}
