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

        val fabAddEntry = findViewById<FloatingActionButton>(R.id.fab_add_entry)
        fabAddEntry.setOnClickListener {
            val intent = Intent(this, AddEntryActivity::class.java)
            startActivity(intent)
        }

        recyclerViewEntries = findViewById(R.id.recyclerViewEntries)
        recyclerViewEntries.layoutManager = LinearLayoutManager(this)

        entriesAdapter = EntriesAdapter(emptyList()) { entry ->
            val intent = Intent(this, AddEntryActivity::class.java)
            intent.putExtra("entry", entry)
            startActivity(intent)
        }
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
            val entries = db.entryDao().getAllEntries().toMutableList()
            entries.sortByDescending { it.date }
            runOnUiThread {
                entriesAdapter.updateEntries(entries)
            }
        }.start()
    }
}
