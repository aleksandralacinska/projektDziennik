package com.example.dziennikmvvm.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.Date

@Dao
interface EntryDao {
    //zapytanie zwracające wpisy z określoną datą
    @Query("SELECT * FROM entries WHERE date = :date")
    fun getEntriesByDate(date: Date): List<Entry>

    //zapytanie zwracające wszystkie wpisy
    @Query("SELECT * FROM entries")
    fun getAllEntries(): List<Entry>

    //zapytanie zwracające wpis o określonym identyfikatorze
    @Query("SELECT * FROM entries WHERE id = :entryId")
    fun getEntryById(entryId: Int): Entry?

    //aktualizacja tytułu i treści wpisu o określonym identyfikatorze
    @Query("UPDATE entries SET title = :title, content = :content WHERE id = :entryId")
    fun updateEntry(entryId: Int, title: String, content: String)

    //usunięcie wpisu
    @Delete
    fun delete(entry: Entry)

    //wstawienie nowego wpisu
    @Insert
    fun insert(entry: Entry)

    //aktualizacja wpisu
    @Update
    fun update(entry: Entry)

}
