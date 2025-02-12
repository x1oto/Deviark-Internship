package com.x1oto.cleanlibraryapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.x1oto.cleanlibraryapp.databinding.ItemReviewBinding
import com.x1oto.domain.model.Review

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private var reviews: List<Review> = emptyList()

    fun setReviews(reviews: List<Review>) {
        this.reviews = reviews
        notifyDataSetChanged()
    }

    class ReviewViewHolder(private val binding: ItemReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(review: Review) {
            binding.reviewTv.text = review.text
            binding.textViewNickname.text = review.nickname
        }

        companion object {
            fun from(parent: ViewGroup): ReviewViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemReviewBinding.inflate(inflater, parent, false)
                return ReviewViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ReviewViewHolder.from(parent)

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount() = reviews.size
}