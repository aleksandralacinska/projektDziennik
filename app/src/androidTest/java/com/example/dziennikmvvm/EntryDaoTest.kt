package com.example.dziennikmvvm

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.dziennikmvvm.model.AppDatabase
import com.example.dziennikmvvm.model.Entry
import com.example.dziennikmvvm.model.EntryDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.Date

@RunWith(AndroidJUnit4::class)
@SmallTest
class EntryDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var entryDao: EntryDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        entryDao = database.entryDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndGetEntry() = runBlocking {
        val entry = Entry(date = Date(), content = "Test entry")
        entryDao.insert(entry)
        val entries = entryDao.getAllEntries()
        assertEquals(1, entries.size)
        assertEquals("Test entry", entries[0].content)
    }

    @Test
    fun updateEntryContent() = runBlocking {
        val entry = Entry(date = Date(), content = "Old content")
        entryDao.insert(entry)
        val insertedEntry = entryDao.getAllEntries().first()
        entryDao.updateEntryContent(insertedEntry.id, "New content")
        val updatedEntry = entryDao.getEntryById(insertedEntry.id)
        assertEquals("New content", updatedEntry?.content)
    }

    @Test
    fun deleteEntry() = runBlocking {
        val entry = Entry(date = Date(), content = "Test entry")
        entryDao.insert(entry)
        val insertedEntry = entryDao.getAllEntries().first()
        entryDao.delete(insertedEntry)
        val entries = entryDao.getAllEntries()
        assertEquals(0, entries.size)
    }

    @Test
    fun getEntriesByDate() = runBlocking {
        val date = Date()
        val entry1 = Entry(date = date, content = "Entry 1")
        val entry2 = Entry(date = Date(date.time + 1000 * 60 * 60 * 24), content = "Entry 2")
        entryDao.insert(entry1)
        entryDao.insert(entry2)
        val entries = entryDao.getEntriesByDate(date)
        assertEquals(1, entries.size)
        assertEquals("Entry 1", entries[0].content)
    }

    @Test
    fun getEntryById() = runBlocking {
        val entry = Entry(date = Date(), content = "Test entry")
        entryDao.insert(entry)
        val insertedEntry = entryDao.getAllEntries().first()
        val fetchedEntry = entryDao.getEntryById(insertedEntry.id)
        assertEquals(insertedEntry.id, fetchedEntry?.id)
        assertEquals(insertedEntry.content, fetchedEntry?.content)
    }

    @Test
    fun updateEntry() = runBlocking {
        val entry = Entry(date = Date(), content = "Old content")
        entryDao.insert(entry)
        val insertedEntry = entryDao.getAllEntries().first()
        val updatedEntry = insertedEntry.copy(content = "New content")
        entryDao.update(updatedEntry)
        val fetchedEntry = entryDao.getEntryById(insertedEntry.id)
        assertEquals("New content", fetchedEntry?.content)
    }
}
