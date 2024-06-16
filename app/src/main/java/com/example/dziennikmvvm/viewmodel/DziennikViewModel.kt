package com.example.dziennikmvvm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.dziennikmvvm.model.AppDatabase
import com.example.dziennikmvvm.model.DziennikData
import com.example.dziennikmvvm.model.Entry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class DziennikViewModel(application: Application) : AndroidViewModel(application) {

    private val _dziennikData = MutableLiveData<DziennikData>()
    val dziennikData: LiveData<DziennikData> get() = _dziennikData

    private val _entries = MutableLiveData<List<Entry>>()
    val entries: LiveData<List<Entry>> get() = _entries

    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "journal-database"
    ).build()

    init {
        _dziennikData.value = DziennikData()
    }

    //ładowanie wszystkich wpisów z bazy danych
    fun loadEntries() {
        viewModelScope.launch {
            val entriesList = withContext(Dispatchers.IO) {
                db.entryDao().getAllEntries()
            }.sortedByDescending { it.date }
            _entries.value = entriesList
        }
    }

    //dodawanie nowego wpisu do bazy danych
    fun addEntry(title: String, content: String) {
        viewModelScope.launch {
            val newEntry = Entry(title = title, date = Date(), content = content)
            withContext(Dispatchers.IO) {
                db.entryDao().insert(newEntry)
            }
            loadEntries() // reload entries after adding a new one
        }
    }

    //aktualizacja wpisu w bazie danych
    fun updateEntry(entryId: Int, title: String, content: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.entryDao().updateEntry(entryId, title, content)
            }
            loadEntries() // reload entries after updating
        }
    }

    //usuwanie wpisu z bazy danych
    fun deleteEntry(entry: Entry) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.entryDao().delete(entry)
            }
            loadEntries() // reload entries after deleting
        }
    }
}
