package com.example.dziennikmvvm.view

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.room.Room
import com.example.dziennikmvvm.R
import com.example.dziennikmvvm.model.AppDatabase
import com.example.dziennikmvvm.model.Entry

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

        val mainLayout = findViewById<ConstraintLayout>(R.id.mainLayout)

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

    //uniwersalna funkcja ukrywająca klawiaturę
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
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
                finish()
            }
        }.start()
    }
}
