package com.example.dziennikmvvm.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.applandeo.materialcalendarview.CalendarView
import com.example.dziennikmvvm.R
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

        // Przykładowa konfiguracja kalendarza
        val today = Calendar.getInstance()
        calendarView.setMinimumDate(today)
        calendarView.setMaximumDate(today)
    }
}
