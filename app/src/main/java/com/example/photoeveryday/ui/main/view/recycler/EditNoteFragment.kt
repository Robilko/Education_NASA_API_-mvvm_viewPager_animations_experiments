package com.example.photoeveryday.ui.main.view.recycler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.photoeveryday.databinding.FragmentEditNoteBinding
import com.example.photoeveryday.ui.main.model.Data

class EditNoteFragment : Fragment() {
    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!
    private var noteData: Data? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (arguments != null) {
            noteData = requireArguments().getSerializable(NOTE_EXTRA_KEY) as Data
        }
        fillNote(noteData)
        binding.saveBtn.setOnClickListener { contract!!.saveNote(collectNote()) }
    }

    private fun collectNote(): Data = Data(noteData!!.id, binding.noteHeading.text.toString(), binding.noteTextBody.text.toString(), noteData!!.viewType)

    private fun fillNote(noteData: Data?) {
        noteData?.let {
            with(binding) {
                noteHeading.setText(it.someText)
                noteTextBody.setText(it.someDescription)
            }
        }
    }

    private val contract: Contract?
        get() = activity as Contract?

    internal interface Contract {
        fun saveNote(data: Data?)
    }

    companion object {
        private const val NOTE_EXTRA_KEY = "NOTE_EXTRA_KEY"
        fun newInstance(data: Data?): EditNoteFragment {
            val fragment = EditNoteFragment()
            val bundle = Bundle()
            bundle.putSerializable(NOTE_EXTRA_KEY, data)
            fragment.arguments = bundle
            return fragment
        }
    }
}