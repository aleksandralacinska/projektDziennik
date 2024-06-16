package com.example.dziennikmvvm.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.dziennikmvvm.R
import com.example.dziennikmvvm.viewmodel.DziennikViewModel
import com.example.dziennikmvvm.notification.NotificationScheduler


class MainActivity : AppCompatActivity() {

    private val correctPin = "1234"
    private lateinit var viewModel: DziennikViewModel

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Notification permission granted
            NotificationScheduler.scheduleDailyNotification(this)
        } else {
            // Inform the user that the permission was not granted
            Toast.makeText(this, "Permission for notifications was not granted", Toast.LENGTH_SHORT).show()
        }
    }

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
                // Sprawdź, czy uprawnienie zostało już przyznane
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    when {
                        ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            NotificationScheduler.scheduleDailyNotification(this) // Zakomentowane
                        }
                        else -> {
                            // Poproś o uprawnienie
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                } else {
                    NotificationScheduler.scheduleDailyNotification(this) // Zakomentowane
                }
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
