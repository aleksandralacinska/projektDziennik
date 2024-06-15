package com.example.dziennikmvvm.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.Date
import androidx.room.Delete

@Dao
interface EntryDao {
    @Query("SELECT * FROM entries WHERE date = :date")
    fun getEntriesByDate(date: Date): List<Entry>

    @Query("SELECT * FROM entries")
    fun getAllEntries(): List<Entry>

    @Query("SELECT * FROM entries WHERE id = :entryId")
    fun getEntryById(entryId: Int): Entry?

    @Query("UPDATE entries SET title = :title, content = :content WHERE id = :entryId")
    fun updateEntry(entryId: Int, title: String, content: String)

    @Delete
    fun delete(entry: Entry)

    @Insert
    fun insert(entry: Entry)
}
