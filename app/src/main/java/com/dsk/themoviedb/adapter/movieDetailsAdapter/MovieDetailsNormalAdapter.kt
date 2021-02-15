package com.dsk.themoviedb.adapter.movieDetailsAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dsk.themoviedb.data.model.MovieDetails
import com.dsk.themoviedb.databinding.MovieDetailViewBinding
import com.dsk.themoviedb.ui.MovieDetailsImpl
import com.dsk.themoviedb.util.APIConstants

class MovieDetailsNormalAdapter(
        private val recipeDetailsImpl: MovieDetailsImpl) :
        RecyclerView.Adapter<MovieDetailsNormalAdapter.CartListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartListHolder {
        val binding = MovieDetailViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartListHolder(binding)
    }

    override fun onBindViewHolder(holder: CartListHolder, position: Int) {
        val currentList = differ.currentList[position]
        holder.itemView.apply {
//            Log.d("DSK ", "${APIConstants.BASE_IMAGE_URL_w500_API}${currentList.poster_path}")
            Glide.with(holder.itemView)
                    .asBitmap()
                    .load("${APIConstants.BASE_IMAGE_URL_w500_API}${currentList.poster_path}")
                    .into(holder.viewBinding.imageViewMovie)
            holder.viewBinding.textViewMovieTitle.text = currentList.title
            holder.viewBinding.cardViewMovieDetails.setOnClickListener {
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