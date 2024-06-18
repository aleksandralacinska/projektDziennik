package com.example.dziennikmvvm.view

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.dziennikmvvm.R
import com.example.dziennikmvvm.model.AppDatabase
import com.example.dziennikmvvm.model.Entry
import java.text.SimpleDateFormat
import java.util.*

class AddEntryActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private var currentLocation: String? = null
    private val locationPermissionCode = 2
    private val CHANNEL_ID = "journal_channel_id"
    private val REQUEST_CODE_POST_NOTIFICATIONS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entry)
        createNotificationChannel()

        // Sprawdzenie i poproszenie o uprawnienia
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_CODE_POST_NOTIFICATIONS)
            }
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        //pobranie aktualnej daty i ustawienie jako tytuł na górnym pasku Toolbar
        val currentDate = getCurrentDate()
        toolbar.title = currentDate
        //obsługa kliknięcia przycisku "powrót"
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val mainLayout = findViewById<ConstraintLayout>(R.id.mainLayout)
        val editTextEntryTitle = findViewById<EditText>(R.id.editTextEntryTitle)
        val editTextEntryContent = findViewById<EditText>(R.id.editTextEntryContent)
        val textViewLocation = findViewById<TextView>(R.id.textViewLocation)
        val buttonSaveEntry = findViewById<Button>(R.id.buttonSaveEntry)
        //ukrywanie klawiatury po kliknięciu na ekran poza nią lub poza polem tekstowym
        mainLayout.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                hideKeyboard()
            }
            false
        }

        editTextEntryContent.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hideKeyboard()
            }
        }
        //zapisywanie wpisu w dzienniku
        buttonSaveEntry.setOnClickListener {
            val title = editTextEntryTitle.text.toString()
            val content = editTextEntryContent.text.toString()
            if (title.isNotBlank() && content.isNotBlank()) {
                saveEntry(title, content, currentLocation)
            } else {
                Toast.makeText(this, "Tytuł i treść wpisu nie mogą być puste", Toast.LENGTH_SHORT).show()
            }
        }

        getLocation()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Uprawnienie do powiadomień przyznane", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Uprawnienie do powiadomień odmówione", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, locationListener)
        }
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val geocoder = Geocoder(this@AddEntryActivity, Locale.getDefault())
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                currentLocation = addresses[0].locality
                findViewById<TextView>(R.id.textViewLocation).text = currentLocation
            }
        }
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }
    //zapisywanie nowego wpisu w bazie danych
    private fun saveEntry(title: String, content: String, location: String?) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "journal-database"
        ).build()

        val newEntry = Entry(title = title, date = Date(), content = content, location = location)

        Thread {
            db.entryDao().insert(newEntry)
            runOnUiThread {
                Toast.makeText(this, "Wpis został zapisany", Toast.LENGTH_SHORT).show()
                sendNotification("Dodano wpis", "Dziękuję za dzisiejszy wpis! Mam nadzieję, że dzień minał ci cudownie!")
                finish()
            }
        }.start()
    }
    //pobranie aktualnej daty w formacie dd-MM-yyyy
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }
    //ukrywanie klawiatury
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Journal Channel"
            val descriptionText = "Channel for journal entry notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(title: String, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Brak uprawnień do wysyłania powiadomień", Toast.LENGTH_SHORT).show()
            return
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }
}
