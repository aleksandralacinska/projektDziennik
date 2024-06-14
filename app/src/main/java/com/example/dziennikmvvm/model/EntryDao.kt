package com.example.dziennikmvvm.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.Date

@Dao
interface EntryDao {
    @Query("SELECT * FROM entries WHERE date = :date")
    fun getEntriesByDate(date: Date): List<Entry>

    @Query("SELECT * FROM entries")
    fun getAllEntries(): List<Entry>

    @Query("SELECT * FROM entries WHERE id = :entryId")
    fun getEntryById(entryId: Int): Entry?

    @Query("UPDATE entries SET content = :content WHERE id = :entryId")
    fun updateEntryContent(entryId: Int, content: String)

    @Insert
    fun insert(entry: Entry)
}
