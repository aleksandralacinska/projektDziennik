package com.example.dziennikmvvm.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.dziennikmvvm.R
import com.example.dziennikmvvm.viewmodel.DziennikViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: DziennikViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewModel = ViewModelProvider(this).get(DziennikViewModel::class.java)

        val welcomeTextView: TextView = findViewById(R.id.welcomeTextView)
        viewModel.dziennikData.observe(this) { data ->
            welcomeTextView.text = data.welcomeMessage
        }

        // Przekierowanie po 3 sekundach
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, JournalActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // 3000 ms = 3 sekundy
    }
}
