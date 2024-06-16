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
import java.text.SimpleDateFormat
import java.util.*

class AddEntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entry)

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
                saveEntry(title, content)
            } else {
                Toast.makeText(this, "Tytuł i treść wpisu nie mogą być puste", Toast.LENGTH_SHORT).show()
            }
        }
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

    //zapisywanie nowego wpisu w bazie danych
    private fun saveEntry(title: String, content: String) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "journal-database"
        ).build()

        val newEntry = Entry(title = title, date = Date(), content = content)

        Thread {
            db.entryDao().insert(newEntry)
            runOnUiThread {
                Toast.makeText(this, "Wpis został zapisany", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.start()
    }
}
