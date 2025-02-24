package com.x1oto.cleanlibraryapp.presentation.adapters

import android.annotation.SuppressLint
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

    class BookViewHolder(val binding: ItemBookBinding) : HomeRecyclerViewHolder(binding) {
        @SuppressLint("SetTextI18n")
        fun bind(
            book: HomeRecyclerViewItem.Book
        ) {
            binding.run {
                textViewTitle.text = book.title
                textViewBorrowCount.text = book.borrowCount.toString()
                textViewAuthor.text = book.author
                textViewYear.text = book.year.toString()
            }
        }
    }

}