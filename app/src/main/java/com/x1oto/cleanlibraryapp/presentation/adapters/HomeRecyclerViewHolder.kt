package com.x1oto.cleanlibraryapp.presentation.adapters

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.x1oto.cleanlibraryapp.databinding.ItemBookBinding
import com.x1oto.cleanlibraryapp.databinding.ItemLetterBinding

sealed class HomeRecyclerViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class LetterViewHolder(private val binding: ItemLetterBinding) : HomeRecyclerViewHolder(binding) {
        fun bind(letter: HomeRecyclerViewItem.Letter) {
            binding.textViewLetter.text = letter.char.toString()
        }
    }

    class BookViewHolder(private val binding: ItemBookBinding) : HomeRecyclerViewHolder(binding) {
        fun bind(book: HomeRecyclerViewItem.Book, onBookClicked: (Long) -> Unit) {
            binding.run {
                textViewTitle.text = book.title
                textViewBorrowCount.text = book.borrowCount.toString()
                textViewAuthor.text = book.author
                textViewYear.text = book.year.toString()

                cardViewBook.setOnClickListener {
                    onBookClicked(book.id)
                }
            }
        }
    }

}