package com.indiahaat.onlinenotes.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.indiahaat.onlinenotes.R
import com.indiahaat.onlinenotes.model.NotesModel
import com.indiahaat.onlinenotes.utility.AppConstants

class HomeViewModel : ViewModel() {
    private val noteList = arrayListOf<NotesModel>()

    fun fetchNotes(): MutableLiveData<List<NotesModel>> {
        val notesMutableLiveData : MutableLiveData<List<NotesModel>> = MutableLiveData()
        val db = FirebaseFirestore.getInstance()
        db.collection(AppConstants.COLLECTION_NOTES).get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    noteList.clear()
                    for (doc in it) {
                        val noteModel = doc.toObject(NotesModel::class.java)
                        noteModel.noteId = doc.id
                        noteList.add(noteModel)
                    }
                }
                notesMutableLiveData.value = noteList
            }
        return notesMutableLiveData
    }

    fun deleteNote(context: Context, note: NotesModel): MutableLiveData<String> {
        val deleteLiveData : MutableLiveData<String> = MutableLiveData()
        val db = FirebaseFirestore.getInstance()
        db.collection(AppConstants.COLLECTION_NOTES).document(note.noteId!!).delete()
            .addOnSuccessListener {
                deleteLiveData.value = context.getString(R.string.delete_success)
            }
            .addOnCanceledListener {
                deleteLiveData.value =  context.getString(R.string.global_err_msz)
            }
        return deleteLiveData
    }
}