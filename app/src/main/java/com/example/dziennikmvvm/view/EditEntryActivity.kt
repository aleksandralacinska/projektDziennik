package com.example.dziennikmvvm.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import com.example.dziennikmvvm.R
import com.example.dziennikmvvm.model.AppDatabase
import com.example.dziennikmvvm.model.Entry
import java.util.*

class EditEntryActivity : AppCompatActivity() {

    private var entryId: Int = 0
    private lateinit var editTextEntryTitle: EditText
    private lateinit var editTextEntryContent: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_entry)

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

    private fun updateEntry(entryId: Int, title: String, content: String) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "journal-database"
        ).build()

        Thread {
            db.entryDao().updateEntry(entryId, title, content)
            runOnUiThread {
                Toast.makeText(this, "Wpis został zaktualizowany", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.start()
    }
}
