package com.x1oto.cleanlibraryapp.presentation.adapters

import androidx.recyclerview.widget.DiffUtil

class DiffUtil(private val oldList: List<HomeRecyclerViewItem>, private val newList: List<HomeRecyclerViewItem>) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}