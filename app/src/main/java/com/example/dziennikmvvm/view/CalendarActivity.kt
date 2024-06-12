package com.example.dziennikmvvm.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.materialcalendarview.CalendarView
import java.util.*
import com.example.dziennikmvvm.R
class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)

        // Przykładowa konfiguracja kalendarza
        val today = Calendar.getInstance()
        calendarView.setMinimumDate(today)
        calendarView.setMaximumDate(today)
    }
}