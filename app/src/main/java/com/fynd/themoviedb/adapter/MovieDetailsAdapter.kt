package com.fynd.themoviedb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fynd.themoviedb.R
import com.fynd.themoviedb.data.model.MovieDetails
import com.fynd.themoviedb.databinding.MovieDetailViewBinding
import com.fynd.themoviedb.ui.MovieDetailsImpl
import com.fynd.themoviedb.util.APIConstants

class MovieDetailsAdapter(
    private val recipeDetailsImpl: MovieDetailsImpl) :
    RecyclerView.Adapter<MovieDetailsAdapter.CartListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartListHolder {
        val binding = MovieDetailViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartListHolder(binding)
    }

    override fun onBindViewHolder(holder: CartListHolder, position: Int) {
        val currentList = differ.currentList[position]
        val itemViewHolder = holder as CartListHolder
        holder.itemView.apply {
            Glide.with(holder.itemView)
                .asBitmap()
                .load("${APIConstants.BASE_IMAGE_URL_API} ${currentList.poster_path}&api_key=${APIConstants.API_KEY}")
                .into(itemViewHolder.viewBinding.imageViewMovie)

            itemViewHolder.viewBinding.textViewMovieTitle.text = currentList.title
            itemViewHolder.viewBinding.cardViewMovieDetails.setOnClickListener {
                recipeDetailsImpl.clickListenerMovieDetailView(currentList)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class CartListHolder(var viewBinding: MovieDetailViewBinding) : RecyclerView.ViewHolder(viewBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<MovieDetails>() {
        override fun areItemsTheSame(oldItem: MovieDetails, newItem: MovieDetails): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieDetails, newItem: MovieDetails): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}