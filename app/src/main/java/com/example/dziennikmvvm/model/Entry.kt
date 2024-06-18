package com.example.dziennikmvvm.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "entries")
data class Entry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val date: Date,
    val content: String,
    val location: String? = null
)
