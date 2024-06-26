package com.example.dziennikmvvm.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.dziennikmvvm.R
import com.example.dziennikmvvm.viewmodel.DziennikViewModel

class MainActivity : AppCompatActivity() {

    private val correctPin = "1234"
    private lateinit var viewModel: DziennikViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(this).get(DziennikViewModel::class.java)

        val editTextPin = findViewById<EditText>(R.id.editTextPin)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)

        //obsługa przycisku "zaloguj"
        buttonSubmit.setOnClickListener {
            val enteredPin = editTextPin.text.toString()
            if (enteredPin == correctPin) {
                navigateToHomeScreen()
            } else {
                Toast.makeText(this, "Niepoprawny PIN", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //przekierowanie do ekranu powitalnego
    private fun navigateToHomeScreen() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
