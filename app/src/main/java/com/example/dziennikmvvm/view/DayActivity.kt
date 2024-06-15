package com.example.dziennikmvvm.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.dziennikmvvm.R
import com.example.dziennikmvvm.model.AppDatabase
import com.example.dziennikmvvm.model.Entry
import java.text.SimpleDateFormat
import java.util.*


class DayActivity : AppCompatActivity() {


    private lateinit var recyclerViewEntries: RecyclerView
    private lateinit var entriesAdapter: EntriesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_day)


        val selectedDate = intent.getLongExtra("selectedDate", 0)
        val date = Date(selectedDate)


        val textViewDate = findViewById<TextView>(R.id.textViewDate)
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        textViewDate.text = dateFormat.format(date)


        recyclerViewEntries = findViewById(R.id.recyclerViewEntries)
        recyclerViewEntries.layoutManager = LinearLayoutManager(this)


        entriesAdapter = EntriesAdapter(emptyList())
        recyclerViewEntries.adapter = entriesAdapter


        loadEntries(date)
    }


    private fun loadEntries(date: Date) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "journal-database"
        ).build()


        Thread {
            val entries = db.entryDao().getEntriesByDate(date)
            runOnUiThread {
                entriesAdapter.updateEntries(entries)
            }
        }.start()
    }
}