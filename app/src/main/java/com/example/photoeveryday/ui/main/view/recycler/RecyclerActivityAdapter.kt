package com.example.photoeveryday.ui.main.view.recycler

import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.photoeveryday.R
import com.example.photoeveryday.ui.main.model.Data
import com.example.photoeveryday.ui.main.utils.TYPE_EARTH
import com.example.photoeveryday.ui.main.utils.TYPE_MARS
import com.example.photoeveryday.ui.main.utils.TYPE_NOTE
import kotlinx.android.synthetic.main.activity_recycler_item_earth.view.*
import kotlinx.android.synthetic.main.activity_recycler_item_mars.view.*
import kotlinx.android.synthetic.main.activity_recycler_item_mars.view.addItemImageView
import kotlinx.android.synthetic.main.activity_recycler_item_mars.view.dragHandleImageView
import kotlinx.android.synthetic.main.activity_recycler_item_mars.view.moveItemDown
import kotlinx.android.synthetic.main.activity_recycler_item_mars.view.moveItemUp
import kotlinx.android.synthetic.main.activity_recycler_item_mars.view.removeItemImageView
import kotlinx.android.synthetic.main.activity_recycler_item_note.view.*

class RecyclerActivityAdapter(
    private val onListItemClickListener: OnListItemClickListener,
    private var data: MutableList<Pair<Data, Boolean>>,
    private val dragListener: OnStartDragListener
) : RecyclerView.Adapter<BaseViewHolder>(), ItemTouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_EARTH -> EarthViewHolder(
                inflater.inflate(
                    R.layout.activity_recycler_item_earth,
                    parent,
                    false
                ) as View
            )

            TYPE_MARS -> MarsViewHolder(
                inflater.inflate(
                    R.layout.activity_recycler_item_mars,
                    parent,
                    false
                ) as View
            )

            TYPE_NOTE -> NoteViewHolder(
                inflater.inflate(
                    R.layout.activity_recycler_item_note,
                    parent,
                    false
                ) as View
            )

            else -> HeaderViewHolder(
                inflater.inflate(R.layout.activity_recycler_item_header, parent, false) as View
            )
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            val combinedChange =
                createCombinedPayload(payloads as List<Change<Pair<Data, Boolean>>>)
            val oldData = combinedChange.oldData
            val newData = combinedChange.newData

            if (newData.first.someText != oldData.first.someText) {
                holder.itemView.note_header.text = newData.first.someText
            }

            if (newData.first.someDescription != oldData.first.someDescription) {
                holder.itemView.note_description.text = newData.first.someDescription
            }
        }
    }

    fun setItems(newItems: List<Pair<Data, Boolean>>) {
        val result = DiffUtil.calculateDiff(DiffUtilCallback(data, newItems))
        result.dispatchUpdatesTo(this)
        data.clear()
        data.addAll(newItems)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].first.viewType
    }

    fun appendItem() {
        data.add(generateItem())
        notifyItemInserted(itemCount - 1)
    }

    private fun generateItem() =
        Pair(Data(Data.generateNewId(), "Заметка", "Новая заметка", TYPE_NOTE), false)

    inner class DiffUtilCallback(
        private var oldItems: List<Pair<Data, Boolean>>,
        private var newItems: List<Pair<Data, Boolean>>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size


        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].first.id == newItems[newItemPosition].first.id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldItems[oldItemPosition].first.someText == newItems[newItemPosition].first.someText && oldItems[oldItemPosition].first.someDescription == newItems[newItemPosition].first.someDescription

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return Change(oldItem, newItem)
        }
    }

    inner class EarthViewHolder(view: View) : BaseViewHolder(view), ItemTouchHelperViewHolder {
        override fun bind(data: Pair<Data, Boolean>) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                itemView.descriptionTextView.text = data.first.someDescription
                itemView.wikiImageView.setOnClickListener { onListItemClickListener.onItemClick(data.first) }
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(itemView.context.resources.getColor(R.color.recycler_item_selected_background))
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(itemView.context.resources.getColor(R.color.app_background))
        }
    }

    inner class MarsViewHolder(view: View) : BaseViewHolder(view), ItemTouchHelperViewHolder {

        override fun bind(data: Pair<Data, Boolean>) {
            itemView.marsImageView.setOnClickListener { onListItemClickListener.onItemClick(data.first) }
            itemView.addItemImageView.setOnClickListener { addItem(data) }
            itemView.removeItemImageView.setOnClickListener { removeItem() }
            itemView.moveItemDown.setOnClickListener { moveDown() }
            itemView.moveItemUp.setOnClickListener { moveUp() }
            itemView.marsDescriptionTextView.visibility =
                if (data.second) View.VISIBLE else View.GONE
            itemView.marsTextView.setOnClickListener { toggleText() }
            itemView.dragHandleImageView.setOnTouchListener { _, event ->
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(this)
                }
                false
            }
        }


        private fun toggleText() {
            data[layoutPosition] = data[layoutPosition].let {
                it.first to !it.second
            }
            notifyItemChanged(layoutPosition)
        }

        private fun moveUp() {
            layoutPosition.takeIf { it > 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition - 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        private fun moveDown() {
            layoutPosition.takeIf { it < data.size - 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition + 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
        }

        private fun removeItem() {
            data.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

        private fun addItem(item: Pair<Data, Boolean>) {
            data.add(layoutPosition, item)
            notifyItemInserted(layoutPosition)
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(itemView.context.resources.getColor(R.color.recycler_item_selected_background))
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(itemView.context.resources.getColor(R.color.app_background))
        }
    }

    inner class NoteViewHolder(view: View) : BaseViewHolder(view), ItemTouchHelperViewHolder {

        override fun bind(data: Pair<Data, Boolean>) {
            itemView.note_header.text = data.first.someText
            itemView.note_description.text = data.first.someDescription
            itemView.setOnClickListener { onListItemClickListener.onItemClick(data.first) }
            itemView.addItemImageView.setOnClickListener { addItem() }
            itemView.removeItemImageView.setOnClickListener { removeItem() }
            itemView.moveItemDown.setOnClickListener { moveDown() }
            itemView.moveItemUp.setOnClickListener { moveUp() }
            itemView.note_description.visibility = if (data.second) View.VISIBLE else View.GONE
            itemView.note_header.setOnClickListener { toggleText() }
            itemView.dragHandleImageView.setOnTouchListener { _, event ->
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(this)
                }
                false
            }
        }


        private fun toggleText() {
            data[layoutPosition] = data[layoutPosition].let {
                it.first to !it.second
            }
            notifyItemChanged(layoutPosition)
        }

        private fun moveUp() {
            layoutPosition.takeIf { it > 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition - 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        private fun moveDown() {
            layoutPosition.takeIf { it < data.size - 1 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition + 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
        }

        private fun removeItem() {
            data.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

        private fun addItem() {
            data.add(layoutPosition, generateItem())
            notifyItemInserted(layoutPosition)
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(itemView.context.resources.getColor(R.color.recycler_item_selected_background))
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(itemView.context.resources.getColor(R.color.app_background))
        }
    }

    inner class HeaderViewHolder(view: View) : BaseViewHolder(view) {
        override fun bind(data: Pair<Data, Boolean>) {
            itemView.setOnClickListener {
                onListItemClickListener.onItemClick(data.first)
            }
        }
    }

    interface OnListItemClickListener {
        fun onItemClick(data: Data)
    }

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }


    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        data.removeAt(fromPosition).apply {
            data.add(toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }
}