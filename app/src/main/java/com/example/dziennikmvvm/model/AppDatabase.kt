package com.example.dziennikmvvm.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

//definicja bazy danych Room
@Database(entities = [Entry::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun entryDao(): EntryDao
}
