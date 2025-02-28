package com.x1oto.cleanlibraryapp.presentation.adapters

import android.app.Dialog
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.x1oto.cleanlibraryapp.R
import com.x1oto.cleanlibraryapp.databinding.ItemBookBinding
import com.x1oto.cleanlibraryapp.databinding.ItemLetterBinding
import com.x1oto.cleanlibraryapp.presentation.mvvm.view.home.ReportBottomSheet


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
    private val selectedBooks = arrayListOf<HomeRecyclerViewItem.Book>()

    private var dialog: Dialog? = null

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
                setupLetterClickListeners(holder, currentLetter)
            }

            is HomeRecyclerViewHolder.BookViewHolder -> {
                val currentBook = items[position] as HomeRecyclerViewItem.Book
                setupBookUI(holder, currentBook)
                applyBookStyle(holder, currentBook)
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
            textViewId.text = currentBook.id.toString()
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
                    applySelection(currentBook)
                } else {
                    onBookClicked(currentBook.id)
                }
            }

            cardViewBook.setOnLongClickListener {
                if (!multiSelection) {
                    multiSelection = true
                    actionMode = requireActivity.startActionMode(this@BookAdapter)
                    applySelection(currentBook)
                    true
                } else {
                    false
                }
            }

            imageViewReport.setOnClickListener {
                val modalBottomSheet = ReportBottomSheet()
                modalBottomSheet.show(requireActivity.supportFragmentManager, "ModalBottomSheet")
            }
        }
    }

    private fun setupLetterClickListeners(
        holder: HomeRecyclerViewHolder.LetterViewHolder,
        currentLetter: HomeRecyclerViewItem.Letter
    ) {
        holder.binding.run {
            cardViewLetter.setOnLongClickListener {
                selectAllBooksStartingWithLetter(currentLetter)
                true
            }

            cardViewLetter.setOnClickListener {
                toggleBooksSelectionByLetter(currentLetter)
            }
        }
    }

    private fun toggleBooksSelectionByLetter(currentLetter: HomeRecyclerViewItem.Letter) {
        val booksToSelect = findBooksThatStartWithSpecificLetter(currentLetter)
        if (multiSelection) {
            if (!selectedBooks.containsAll(booksToSelect)) {
                addAllBook(booksToSelect)
            } else {
                selectedBooks.removeAll(booksToSelect)
                if (selectedBooks.isEmpty()) {
                    actionMode?.finish()
                    multiSelection = false
                }
            }
        }
        notifyDataSetChanged()
    }

    private fun selectAllBooksStartingWithLetter(currentLetter: HomeRecyclerViewItem.Letter) {
        val booksToSelect = findBooksThatStartWithSpecificLetter(currentLetter)
        if (!multiSelection) {
            multiSelection = true
            actionMode = requireActivity.startActionMode(this@BookAdapter)
            addAllBook(booksToSelect)
        }
        notifyDataSetChanged()
    }

    private fun findBooksThatStartWithSpecificLetter(currentLetter: HomeRecyclerViewItem.Letter): List<HomeRecyclerViewItem.Book> {
        val booksToSelect = items.filterIsInstance<HomeRecyclerViewItem.Book>()
            .filter { it.title.startsWith(currentLetter.char, ignoreCase = true) }
        return booksToSelect
    }

    private fun addAllBook(booksToSelect: List<HomeRecyclerViewItem.Book>) {
        booksToSelect.forEach { book ->
            if (!selectedBooks.contains(book)) {
                selectedBooks.add(book)
            }
        }
    }

    private fun applyBookStyle(
        holder: HomeRecyclerViewHolder.BookViewHolder,
        currentBook: HomeRecyclerViewItem.Book
    ) {
        val isSelected = selectedBooks.contains(currentBook)
        val backgroundColor = if (isSelected) R.color.md_theme_inversePrimary_mediumContrast
        else R.color.md_theme_background
        val strokeColor = if (isSelected) R.color.md_theme_inverseSurface
        else R.color.md_theme_scrim
        changeBookStyle(holder, backgroundColor, strokeColor)
    }

    private fun applySelection(
        currentBook: HomeRecyclerViewItem.Book
    ) {
        if (selectedBooks.contains(currentBook)) {
            selectedBooks.remove(currentBook)
            finishActionIfListEmpty()
        } else {
            selectedBooks.add(currentBook)
        }
        notifyDataSetChanged()
    }

    private fun finishActionIfListEmpty() {
        if (selectedBooks.isEmpty()) actionMode?.finish()
    }

    private fun changeBookStyle(
        holder: HomeRecyclerViewHolder.BookViewHolder,
        backgroundColor: Int,
        strokeColor: Int
    ) {
        holder.binding.run {
            cardViewBook.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity,
                    backgroundColor
                )
            )
            cardViewBook.strokeColor = strokeColor
        }
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
        if (selectedBooks.size >= 2) {
            buildWarningDialog()
        } else {
            deleteSelectedBooks()
        }
        return true
    }

    private fun buildWarningDialog() {
        dialog = Dialog(requireActivity)
        dialog?.let { window ->
            window.setContentView(R.layout.dialog_are_you_sure)
            window.setCancelable(false)
            window.findViewById<Button>(R.id.buttonDelete)?.setOnClickListener {
                deleteSelectedBooks()
                window.cancel()
            }
            window.findViewById<Button>(R.id.buttonCancel)?.setOnClickListener {
                window.cancel()
            }
            window.show()
        }
    }

    private fun deleteSelectedBooks() {
        onDeleteBook(selectedBooks.map { it.id })
        selectedBooks.clear()
        finishActionIfListEmpty()
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        selectedBooks.clear()
        multiSelection = false
        notifyDataSetChanged()
    }
}