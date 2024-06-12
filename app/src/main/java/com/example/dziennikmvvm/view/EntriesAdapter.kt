package com.example.dziennikmvvm.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dziennikmvvm.R
import com.example.dziennikmvvm.model.Entry

class EntriesAdapter(private var entries: List<Entry>) : RecyclerView.Adapter<EntriesAdapter.EntryViewHolder>() {

    inner class EntryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewContent: TextView = view.findViewById(R.id.textViewContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_entry, parent, false)
        return EntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val entry = entries[position]
        holder.textViewContent.text = entry.content
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    fun updateEntries(newEntries: List<Entry>) {
        entries = newEntries
        notifyDataSetChanged()
    }
}
