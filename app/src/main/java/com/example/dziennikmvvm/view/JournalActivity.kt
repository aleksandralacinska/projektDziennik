package com.example.dziennikmvvm.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dziennikmvvm.R
import com.example.dziennikmvvm.viewmodel.DziennikViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class JournalActivity : AppCompatActivity() {

    private lateinit var recyclerViewEntries: RecyclerView
    private lateinit var entriesAdapter: EntriesAdapter
    private val viewModel: DziennikViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //obsługa kliknięcia przycisku "+"
        val fabAddEntry = findViewById<FloatingActionButton>(R.id.fab_add_entry)
        fabAddEntry.setOnClickListener {
            val intent = Intent(this, AddEntryActivity::class.java)
            startActivity(intent)
        }

        recyclerViewEntries = findViewById(R.id.recyclerViewEntries)
        recyclerViewEntries.layoutManager = LinearLayoutManager(this)

        entriesAdapter = EntriesAdapter(emptyList())
        recyclerViewEntries.adapter = entriesAdapter

        viewModel.entries.observe(this) { entries ->
            entriesAdapter.updateEntries(entries)
        }
        viewModel.loadEntries()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadEntries()
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
}
