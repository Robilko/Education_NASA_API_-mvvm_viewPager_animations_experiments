package com.example.photoeveryday.ui.main.view.recycler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.photoeveryday.databinding.FragmentRecyclerNoteListBinding
import com.example.photoeveryday.ui.main.model.Data
import com.example.photoeveryday.ui.main.utils.*

class NoteListFragment : Fragment() {
    private var _binding: FragmentRecyclerNoteListBinding? = null
    private val binding get() = _binding!!
    lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var adapter: RecyclerActivityAdapter
    private val dataList = arrayListOf(
        Pair(Data(Data.generateNewId(), "Mars", "", TYPE_MARS), false),
        Pair(Data(Data.generateNewId(), "Earth", viewType = TYPE_EARTH), false)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecyclerNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dataList.add(0, Pair(Data(Data.generateNewId(), "Header", viewType = TYPE_HEADER), false))

        adapter = RecyclerActivityAdapter(
            object : RecyclerActivityAdapter.OnListItemClickListener {
                override fun onItemClick(data: Data) {
                    when (data.viewType) {
                        TYPE_NOTE -> {
                            contract!!.editNote(data)
                        }
                        else -> {
                            Toast.makeText(requireActivity(), data.someText, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            },
            dataList,
            object : RecyclerActivityAdapter.OnStartDragListener {
                override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                    itemTouchHelper.startDrag(viewHolder)
                }
            }
        )

        with(binding) {
            recyclerView.adapter = adapter
            recyclerActivityFAB.setOnClickListener { adapter.appendItem() }
            itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }
    }

    fun addNote(newData: Data?) {
        val newList = arrayListOf<Pair<Data, Boolean>>()
        newData?.let {
            for (note in dataList) {
                if (note.first.id == it.id) {
                    newList.add(Pair(Data(it.id, newData.someText, newData.someDescription, it.viewType), false))
                } else {newList.add(note)}
            }

        }
        adapter.setItems(newList.map { it })
    }

    private val contract: Contract?
        get() = activity as Contract?

    internal interface Contract {
        fun editNote(data: Data?)
    }
}