package com.indiahaat.onlinenotes.view

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.indiahaat.onlinenotes.R
import com.indiahaat.onlinenotes.utility.AppConstants
import com.indiahaat.onlinenotes.viewmodel.AddNoteViewModel
import kotlinx.android.synthetic.main.activity_add_note.*
import java.util.*

class AddNoteActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var addNoteViewModel: AddNoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        addNoteViewModel = AddNoteViewModel()
        db = FirebaseFirestore.getInstance()
        btn_save.setOnClickListener {
            saveNote()
        }
    }

    private fun saveNote() {
        if (!isValidated()) return

        val notesIdRef = db.collection(AppConstants.COLLECTION_NOTES).document()
        val notesMap: MutableMap<String, Any> = HashMap()
        notesMap[AppConstants.KEY_TITLE] = et_title.text.toString()
        notesMap[AppConstants.KEY_NOTE_ID] = notesIdRef.id
        notesMap[AppConstants.KEY_NOTE_DESC] = et_desc.text.toString()
        progressbar.visibility = View.VISIBLE
        addNoteViewModel.saveNote(this, notesIdRef, notesMap)
            .observe(this, androidx.lifecycle.Observer {
                if (it == getString(R.string.note_success_msz)) {
                    et_desc.setText(AppConstants.EMPTY)
                    et_title.setText(AppConstants.EMPTY)
                    et_title.requestFocus()
                }
                progressbar.visibility = View.GONE
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            })
    }

    private fun isValidated(): Boolean {
        til_title.isErrorEnabled = false
        til_desc.isErrorEnabled = false
        var isValid = true
        if (TextUtils.isEmpty(et_title.text.toString())) {
            isValid = false
            til_title.error = getString(R.string.title_err)
        }
        if (TextUtils.isEmpty(et_desc.text.toString())) {
            isValid = false
            til_desc.error = getString(R.string.desc_err)
        }
        return isValid
    }
}