package com.indiahaat.onlinenotes.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.indiahaat.onlinenotes.R
import com.indiahaat.onlinenotes.model.NotesModel
import com.indiahaat.onlinenotes.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class HomeActivity : AppCompatActivity(), NotesAdapter.AdapterCallback {

    private lateinit var notesAdapter: NotesAdapter
    private var noteList = arrayListOf<NotesModel>()
    private lateinit var db :FirebaseFirestore
    private lateinit var homeViewModel:HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        db= FirebaseFirestore.getInstance()
        val linearLayoutManager = LinearLayoutManager(this)
        rv_notes.layoutManager = linearLayoutManager
        notesAdapter = NotesAdapter(noteList, this)
        fab.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }
        homeViewModel = HomeViewModel()

    }

    override fun onResume() {
        super.onResume()
        progressbar.visibility = View.VISIBLE
        homeViewModel.fetchNotes().observe(this, Observer {
            noteList = it as ArrayList<NotesModel>
            updateScreen(noteList)
            progressbar.visibility = View.GONE
        })
    }


    private fun updateScreen(noteList: List<NotesModel>) {
        notesAdapter.updateNotes(noteList)
        rv_notes.adapter = notesAdapter
    }

    override fun deleteNote(note: NotesModel, position: Int) {
        progressbar.visibility = View.VISIBLE
        homeViewModel.deleteNote(this, note).observe(this, Observer {
            if (it == getString(R.string.delete_success)) {
                noteList.remove(note)
                updateScreen(noteList)
            }
            progressbar.visibility = View.GONE
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
    }
}