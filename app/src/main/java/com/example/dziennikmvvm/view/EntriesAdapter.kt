package com.example.dziennikmvvm.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import android.content.Intent
import android.app.AlertDialog
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.recyclerview.widget.RecyclerView
import com.example.dziennikmvvm.R
import com.example.dziennikmvvm.model.Entry
import com.example.dziennikmvvm.model.AppDatabase
import java.text.SimpleDateFormat
import java.util.*

class EntriesAdapter(private var entries: List<Entry>, private val onEntryClick: (Entry) -> Unit) : RecyclerView.Adapter<EntriesAdapter.EntryViewHolder>() {

    inner class EntryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
        holder.textViewDate.text = dateFormat.format(entry.date)
        holder.textViewContent.text = entry.content

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

    fun updateEntries(newEntries: List<Entry>) {
        entries = newEntries
        notifyDataSetChanged()
    }

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
            }
        }.start()
    }
}
