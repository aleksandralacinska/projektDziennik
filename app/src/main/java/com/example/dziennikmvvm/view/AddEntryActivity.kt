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

    private var entryId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entry)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val currentDate = getCurrentDate()
        toolbar.title = currentDate

        toolbar.setNavigationOnClickListener {
            finish()
        }

        val mainLayout = findViewById<ConstraintLayout>(R.id.mainLayout)
        val editTextEntryContent = findViewById<EditText>(R.id.editTextEntryContent)
        val buttonSaveEntry = findViewById<Button>(R.id.buttonSaveEntry)
        val buttonUpdateEntry = findViewById<Button>(R.id.buttonUpdateEntry)
        val buttonDeleteEntry = findViewById<Button>(R.id.buttonDeleteEntry)

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

        buttonSaveEntry.setOnClickListener {
            val content = editTextEntryContent.text.toString()
            if (content.isNotBlank()) {
                saveEntry(content)
            } else {
                Toast.makeText(this, "Treść wpisu nie może być pusta", Toast.LENGTH_SHORT).show()
            }
        }

        buttonUpdateEntry.setOnClickListener {
            val content = editTextEntryContent.text.toString()
            if (content.isNotBlank()) {
                updateEntry(content)
            } else {
                Toast.makeText(this, "Treść wpisu nie może być pusta", Toast.LENGTH_SHORT).show()
            }
        }

        buttonDeleteEntry.setOnClickListener {
            if (entryId != -1) {
                deleteEntry()
            }
        }

        val entry = intent.getParcelableExtra<Entry>("entry")
        entry?.let {
            entryId = it.id
            editTextEntryContent.setText(it.content)
            buttonUpdateEntry.visibility = View.VISIBLE
            buttonDeleteEntry.visibility = View.VISIBLE
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    private fun saveEntry(content: String) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "journal-database"
        ).build()

        val newEntry = Entry(date = Date(), content = content)

        Thread {
            db.entryDao().insert(newEntry)
            runOnUiThread {
                Toast.makeText(this, "Wpis został zapisany", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.start()
    }

    private fun updateEntry(content: String) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "journal-database"
        ).build()

        val updatedEntry = Entry(id = entryId, date = Date(), content = content)

        Thread {
            db.entryDao().update(updatedEntry)
            runOnUiThread {
                Toast.makeText(this, "Wpis został zaktualizowany", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.start()
    }

    private fun deleteEntry() {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "journal-database"
        ).build()

        val entryToDelete = Entry(id = entryId, date = Date(), content = "")

        Thread {
            db.entryDao().delete(entryToDelete)
            runOnUiThread {
                Toast.makeText(this, "Wpis został usunięty", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.start()
    }
}
