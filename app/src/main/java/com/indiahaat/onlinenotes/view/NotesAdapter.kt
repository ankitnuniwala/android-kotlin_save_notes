package com.indiahaat.onlinenotes.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.indiahaat.onlinenotes.R
import com.indiahaat.onlinenotes.model.NotesModel

class NotesAdapter(
    private var notes: List<NotesModel>,
    private val adapterCallback: AdapterCallback
) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes_row, parent, false)
        return NotesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bindViews(position)

    }

    fun updateNotes(noteList: List<NotesModel>) {
        this.notes = noteList
        notifyDataSetChanged()
    }

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViews(position: Int) {
            val tvNoteTitle = itemView.findViewById<TextView>(R.id.tv_note_title)
            val tvNoteDesc = itemView.findViewById<TextView>(R.id.tv_note_desc)
            val ivDelete = itemView.findViewById<ImageView>(R.id.iv_delete)
            tvNoteDesc.text = notes[position].noteDescription
            tvNoteTitle.text = notes[position].noteTitle
            ivDelete.setOnClickListener {
                adapterCallback.deleteNote(notes[position], position)
            }
        }
    }

    interface AdapterCallback {
        fun deleteNote(note: NotesModel, position: Int)
    }
}