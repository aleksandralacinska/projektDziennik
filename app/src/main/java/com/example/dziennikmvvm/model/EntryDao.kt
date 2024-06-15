package com.example.dziennikmvvm.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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

    @Query("UPDATE entries SET content = :content WHERE id = :entryId")
    fun updateEntryContent(entryId: Int, content: String)

    @Delete
    fun delete(entry: Entry)

    @Insert
    fun insert(entry: Entry)
    @Update
    fun update(entry: Entry)

    @Delete
    fun delete(entry: Entry)
}
