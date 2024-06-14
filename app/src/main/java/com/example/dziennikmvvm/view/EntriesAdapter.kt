package com.example.dziennikmvvm.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.dziennikmvvm.R
import com.example.dziennikmvvm.model.Entry
import java.text.SimpleDateFormat
import java.util.*

class EntriesAdapter(private var entries: List<Entry>) : RecyclerView.Adapter<EntriesAdapter.EntryViewHolder>() {

    inner class EntryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewContent: TextView = view.findViewById(R.id.textViewContent)
        val textViewDate: TextView = view.findViewById(R.id.textViewDate)
        val buttonEditEntry: Button = view.findViewById(R.id.buttonEditEntry)
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
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    fun updateEntries(newEntries: List<Entry>) {
        entries = newEntries
        notifyDataSetChanged()
    }
}
