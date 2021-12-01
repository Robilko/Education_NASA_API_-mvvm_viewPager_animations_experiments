package com.example.photoeveryday.ui.main.view.recycler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.photoeveryday.R
import com.example.photoeveryday.ui.main.model.Data
import com.example.photoeveryday.ui.main.utils.*

class RecyclerActivity : AppCompatActivity(), NoteListFragment.Contract, EditNoteFragment.Contract{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.notes_container, NoteListFragment(), NOTE_LIST_FRAGMENT)
                .commit()
        }
    }

    override fun editNote(data: Data?) {
        supportFragmentManager.beginTransaction()
            .add(R.id.notes_container, EditNoteFragment.newInstance(data))
            .addToBackStack(EDIT_NOTE_FRAGMENT)
            .commit()
    }

    override fun saveNote(data: Data?) {
        supportFragmentManager.popBackStack()
        val noteListFragment: NoteListFragment = supportFragmentManager.findFragmentByTag(NOTE_LIST_FRAGMENT) as NoteListFragment
        noteListFragment.addNote(data)
    }

}