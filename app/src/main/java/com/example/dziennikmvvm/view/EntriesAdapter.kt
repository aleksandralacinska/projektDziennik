package com.example.dziennikmvvm.view

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.dziennikmvvm.R
import com.example.dziennikmvvm.model.AppDatabase
import com.example.dziennikmvvm.model.Entry
import java.text.SimpleDateFormat
import java.util.*

class EntriesAdapter(private var entries: List<Entry>) : RecyclerView.Adapter<EntriesAdapter.EntryViewHolder>() {

    private val CHANNEL_ID = "journal_channel_id"

    inner class EntryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        val textViewLocation: TextView = view.findViewById(R.id.textViewLocation)
        val textViewContent: TextView = view.findViewById(R.id.textViewContent)
        val textViewDate: TextView = view.findViewById(R.id.textViewDate)
        val buttonEditEntry: Button = view.findViewById(R.id.buttonEditEntry)
        val buttonDeleteEntry: Button = view.findViewById(R.id.buttonDeleteEntry)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_entry, parent, false)
        return EntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val entry = entries[position]
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        holder.textViewTitle.text = entry.title
        holder.textViewLocation.text = entry.location
        holder.textViewDate.text = dateFormat.format(entry.date)
        holder.textViewContent.text = entry.content
        //obsługa przycisku edycji wpisu
        holder.buttonEditEntry.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, EditEntryActivity::class.java)
            intent.putExtra("entryId", entry.id)
            context.startActivity(intent)
        }

        holder.buttonDeleteEntry.setOnClickListener {
            showDeleteConfirmationDialog(holder.itemView.context, entry, position)
        }
    }

    override fun getItemCount(): Int {
        return entries.size
    }
    //aktualizacja listy wpisów
    fun updateEntries(newEntries: List<Entry>) {
        entries = newEntries
        notifyDataSetChanged()
    }
    //okienko modalne przy usuwaniu wpisu
    private fun showDeleteConfirmationDialog(context: Context, entry: Entry, position: Int) {
        AlertDialog.Builder(context).apply {
            setTitle("Usuń wpis")
            setMessage("Czy na pewno chcesz usunąć wpis?")
            setPositiveButton("Tak") { _, _ ->
                deleteEntry(context, entry, position)
            }
            setNegativeButton("Nie", null)
        }.create().show()
    }
    //usuwanie wpisu z bazy danych
    private fun deleteEntry(context: Context, entry: Entry, position: Int) {
        val db = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java, "journal-database"
        ).build()

        Thread {
            db.entryDao().delete(entry)
            (context as? JournalActivity)?.runOnUiThread {
                val updatedEntries = entries.toMutableList()
                updatedEntries.removeAt(position)
                updateEntries(updatedEntries)
                sendNotification(context, "Usunięto wpis", "Wpis pomyślnie usunięty")
            }
        }.start()
    }

    private fun sendNotification(context: Context, title: String, message: String) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Brak uprawnień do wysyłania powiadomień", Toast.LENGTH_SHORT).show()
            return
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }
    }
}
