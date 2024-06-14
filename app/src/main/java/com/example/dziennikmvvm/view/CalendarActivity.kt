package com.example.dziennikmvvm.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.EventDay
import com.example.dziennikmvvm.R
import com.example.dziennikmvvm.model.AppDatabase
import com.example.dziennikmvvm.model.Entry
import java.util.*

class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Obsługa kliknięcia przycisku "powrót"
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        // Konfiguracja kalendarza
        val minDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2000)
        }
        val maxDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, 3000)
        }

        calendarView.setMinimumDate(minDate)
        calendarView.setMaximumDate(maxDate)

        loadEntriesAndMarkDates(calendarView)
    }

    private fun loadEntriesAndMarkDates(calendarView: CalendarView) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "journal-database"
        ).build()

        Thread {
            val entries = db.entryDao().getAllEntries()
            val markedDates = entries.map { entry ->
                val calendar = Calendar.getInstance()
                calendar.time = entry.date
                EventDay(calendar, com.google.android.material.R.drawable.ic_mtrl_checked_circle)
            }
            runOnUiThread {
                calendarView.setEvents(markedDates)
            }
        }.start()
    }
}
