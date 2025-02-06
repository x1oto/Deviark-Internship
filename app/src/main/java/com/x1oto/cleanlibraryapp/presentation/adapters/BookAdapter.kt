package com.x1oto.cleanlibraryapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.x1oto.cleanlibraryapp.databinding.ItemBookBinding
import com.x1oto.domain.model.Book

class BookAdapter(val onBookClicked: (Long) -> Unit) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private var books: List<Book> = emptyList()

    fun setBooks(books: List<Book>) {
        this.books = books
        notifyDataSetChanged()
    }

    class BookViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book, onBookClicked: (Long) -> Unit) {
            binding.run {
                titleTv.text = book.title
                countTv.text = book.borrowCount.toString()
                authorTv.text = book.author
                yearTv.text = book.year.toString()

                itemCv.setOnClickListener {
                    onBookClicked(book.id)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): BookViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemBookBinding.inflate(layoutInflater, parent, false)
                return BookViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = BookViewHolder.from(parent)

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position], onBookClicked)
    }

    override fun getItemCount() = books.size
}