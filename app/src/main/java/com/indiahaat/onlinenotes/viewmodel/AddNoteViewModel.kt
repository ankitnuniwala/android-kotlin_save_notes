package com.indiahaat.onlinenotes.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.indiahaat.onlinenotes.R

class AddNoteViewModel: ViewModel() {

    fun saveNote(context: Context, notesIdRef: DocumentReference,
                notesMap:  MutableMap<String, Any>): MutableLiveData<String> {
        val deleteLiveData : MutableLiveData<String> = MutableLiveData()
        val db = FirebaseFirestore.getInstance()
        val batch = db.batch()
        batch.set(notesIdRef,notesMap)
        batch.commit().addOnSuccessListener {
            deleteLiveData.value = context.getString(R.string.note_success_msz)

        }.addOnFailureListener {
            deleteLiveData.value = context.getString(R.string.global_err_msz)
        }
        return deleteLiveData
    }
}