package com.x1oto.cleanlibraryapp.presentation.adapters

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class SwipeCallback(private val onSwiped: (Long) -> Unit) :
    ItemTouchHelper.Callback() {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        target: ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
        if (viewHolder is BookAdapter.HomeRecyclerViewHolder.BookViewHolder) {
            onSwiped(viewHolder.binding.textViewId.text.toString().toLong())
        }
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: ViewHolder): Int {
        return if (viewHolder is BookAdapter.HomeRecyclerViewHolder.BookViewHolder) {
            makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        } else {
            makeMovementFlags(0, 0)
        }
    }
}