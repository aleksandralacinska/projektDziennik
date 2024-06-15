package com.example.dziennikmvvm.view


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.dziennikmvvm.R
import com.example.dziennikmvvm.model.AppDatabase
import com.example.dziennikmvvm.model.Entry
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class JournalActivity : AppCompatActivity() {


    private lateinit var recyclerViewEntries: RecyclerView
    private lateinit var entriesAdapter: EntriesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        // Obsługa kliknięcia przycisku "+"
        val fabAddEntry = findViewById<FloatingActionButton>(R.id.fab_add_entry)
        fabAddEntry.setOnClickListener {
            val intent = Intent(this, AddEntryActivity::class.java)
            startActivity(intent)
        }


        recyclerViewEntries = findViewById(R.id.recyclerViewEntries)
        recyclerViewEntries.layoutManager = LinearLayoutManager(this)


        entriesAdapter = EntriesAdapter(emptyList())
        recyclerViewEntries.adapter = entriesAdapter


        loadEntries()
    }


    override fun onResume() {
        super.onResume()
        loadEntries()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_journal, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_right_icon -> {
                // Start CalendarActivity when the icon is clicked
                val intent = Intent(this, CalendarActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun loadEntries() {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "journal-database"
        ).build()


        Thread {
            val entries = db.entryDao().getAllEntries().toMutableList() // Konwertuj na mutowalną listę
            entries.sortByDescending { it.date } // Sortowanie wpisów w odwrotnej kolejności chronologicznej
            runOnUiThread {
                entriesAdapter.updateEntries(entries)
            }
        }.start()
    }
}
