package com.x1oto.cleanlibraryapp.presentation.adapters

import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.x1oto.cleanlibraryapp.R
import com.x1oto.cleanlibraryapp.databinding.ItemBookBinding
import com.x1oto.cleanlibraryapp.databinding.ItemLetterBinding


class BookAdapter(
    private val requireActivity: FragmentActivity,
    private val onBookClicked: (Long) -> Unit,
    private val onDeleteBook: (List<Long>) -> Unit
) : RecyclerView.Adapter<BookAdapter.HomeRecyclerViewHolder>(), ActionMode.Callback {

    sealed class HomeRecyclerViewHolder(binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        class LetterViewHolder(val binding: ItemLetterBinding) : HomeRecyclerViewHolder(binding)
        class BookViewHolder(val binding: ItemBookBinding) : HomeRecyclerViewHolder(binding)
    }

    private var items = listOf<HomeRecyclerViewItem>()

    private var actionMode: ActionMode? = null

    private var multiSelection = false
    private var bookViewHolder = arrayListOf<HomeRecyclerViewHolder.BookViewHolder>()
    private val selectedBooks = arrayListOf<HomeRecyclerViewItem.Book>()

    fun setData(newList: List<HomeRecyclerViewItem>) {
        val diffUtil = DiffUtil(items, newList)
        val diffResults = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        items = newList
        diffResults.dispatchUpdatesTo(this@BookAdapter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecyclerViewHolder {
        return when (viewType) {
            R.layout.item_letter -> HomeRecyclerViewHolder.LetterViewHolder(
                ItemLetterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            R.layout.item_book -> HomeRecyclerViewHolder.BookViewHolder(
                ItemBookBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: HomeRecyclerViewHolder, position: Int) {
        when (holder) {
            is HomeRecyclerViewHolder.LetterViewHolder -> {
                val currentLetter = items[position] as HomeRecyclerViewItem.Letter
                setupLetterUI(holder, currentLetter)
            }

            is HomeRecyclerViewHolder.BookViewHolder -> {
                val currentBook = items[position] as HomeRecyclerViewItem.Book
                setupBookUI(holder, currentBook)
                bookViewHolder.add(holder)

                setupBookClickListeners(holder, currentBook)
            }
        }
    }

    private fun setupLetterUI(
        holder: HomeRecyclerViewHolder.LetterViewHolder,
        currentLetter: HomeRecyclerViewItem.Letter
    ) {
        holder.binding.textViewLetter.text = currentLetter.char.toString()
    }

    private fun setupBookUI(
        holder: HomeRecyclerViewHolder.BookViewHolder,
        currentBook: HomeRecyclerViewItem.Book
    ) {
        holder.binding.run {
            textViewTitle.text = currentBook.title
            textViewYear.text = currentBook.year.toString()
            textViewBorrowCount.text = currentBook.borrowCount.toString()
            textViewAuthor.text = currentBook.author
        }
    }

    private fun setupBookClickListeners(
        holder: HomeRecyclerViewHolder.BookViewHolder,
        currentBook: HomeRecyclerViewItem.Book
    ) {
        holder.binding.run {
            cardViewBook.setOnClickListener {
                if (multiSelection) {
                    applySelection(holder, currentBook)
                } else {
                    onBookClicked(currentBook.id)
                }
            }

            cardViewBook.setOnLongClickListener {
                if (!multiSelection) {
                    multiSelection = true
                    actionMode = requireActivity.startActionMode(this@BookAdapter)
                    applySelection(holder, currentBook)
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun applySelection(
        holder: HomeRecyclerViewHolder.BookViewHolder,
        currentBook: HomeRecyclerViewItem.Book
    ) {
        if (selectedBooks.contains(currentBook)) {
            selectedBooks.remove(currentBook)
            changeBookStyle(holder, R.color.md_theme_background, R.color.md_theme_onBackground)
            finishActionIfListEmpty()
        } else {
            selectedBooks.add(currentBook)
            changeBookStyle(
                holder,
                R.color.md_theme_inversePrimary_mediumContrast,
                R.color.md_theme_onPrimaryFixedVariant
            )
        }
    }

    private fun finishActionIfListEmpty() {
        if(selectedBooks.size == 0) actionMode?.finish()
    }

    private fun changeBookStyle(
        holder: HomeRecyclerViewHolder.BookViewHolder,
        backgroundColor: Int,
        strokeColor: Int
    ) {
        holder.binding.cardViewBook.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity,
                backgroundColor
            )
        )
        holder.binding.cardViewBook.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    override fun getItemCount() = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is HomeRecyclerViewItem.Letter -> R.layout.item_letter
            is HomeRecyclerViewItem.Book -> R.layout.item_book
        }
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.home_contextual_menu, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        onDeleteBook(selectedBooks.map { it.id })
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        bookViewHolder.forEach { holder ->
            changeBookStyle(holder, R.color.md_theme_background, R.color.md_theme_onBackground)
        }
        multiSelection = false
        selectedBooks.clear()
    }
}