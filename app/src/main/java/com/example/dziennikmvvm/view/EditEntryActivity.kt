package com.example.dziennikmvvm.view

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.dziennikmvvm.R
import com.example.dziennikmvvm.model.AppDatabase
import com.example.dziennikmvvm.model.Entry

class EditEntryActivity : AppCompatActivity() {

    private var entryId: Int = 0
    private lateinit var editTextEntryTitle: EditText
    private lateinit var editTextEntryContent: EditText
    private val CHANNEL_ID = "journal_channel_id"
    private val REQUEST_CODE_POST_NOTIFICATIONS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_entry)
        createNotificationChannel()

        entryId = intent.getIntExtra("entryId", 0)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        editTextEntryTitle = findViewById(R.id.editTextEntryTitle)
        editTextEntryContent = findViewById(R.id.editTextEntryContent)
        val buttonSaveEntry = findViewById<Button>(R.id.buttonSaveEntry)

        loadEntry(entryId)

        buttonSaveEntry.setOnClickListener {
            val title = editTextEntryTitle.text.toString()
            val content = editTextEntryContent.text.toString()
            if (content.isNotBlank()) {
                updateEntry(entryId, title, content)
            } else {
                Toast.makeText(this, "Tytuł i treść wpisu nie mogą być puste", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //ładowanie danych wpisu po jego id
    private fun loadEntry(entryId: Int) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "journal-database"
        ).build()

        Thread {
            val entry = db.entryDao().getEntryById(entryId)
            runOnUiThread {
                editTextEntryTitle.setText(entry?.title)
                editTextEntryContent.setText(entry?.content)
            }
        }.start()
    }
    //aktualizacja wpisu w bazie danych
    private fun updateEntry(entryId: Int, title: String, content: String) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "journal-database"
        ).build()

        Thread {
            db.entryDao().updateEntry(entryId, title, content)
            runOnUiThread {
                Toast.makeText(this, "Wpis został zaktualizowany", Toast.LENGTH_SHORT).show()
                sendNotification("Aktualizacja wpisu", "Wpis pomyślnie zaaktualizowany")
                finish()
            }
        }.start()
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
