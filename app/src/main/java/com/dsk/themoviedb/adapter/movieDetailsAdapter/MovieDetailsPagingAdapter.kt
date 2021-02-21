package com.dsk.themoviedb.adapter.movieDetailsAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dsk.themoviedb.data.model.MovieDetails
import com.dsk.themoviedb.databinding.MovieDetailViewBinding
import com.dsk.themoviedb.ui.MovieDetailsImpl
import com.dsk.themoviedb.util.APIConstants

class MovieDetailsPagingAdapter(private val recipeDetailsImpl: MovieDetailsImpl)
    : PagingDataAdapter<MovieDetails, MovieDetailsPagingAdapter.ViewHolder>(DataDifferntiator) {

    class ViewHolder(var viewBinding: MovieDetailViewBinding) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            Glide.with(holder.itemView)
                .asBitmap()
                .load("${APIConstants.BASE_IMAGE_URL_w500_API}${getItem(position)?.poster_path}")
                .into(holder.viewBinding.imageViewMovie)
//            Log.d("DSK ", "${APIConstants.BASE_IMAGE_URL_w500_API}${currentList.poster_path}")
            holder.viewBinding.textViewMovieTitle.text = getItem(position)?.title
            holder.viewBinding.cardViewMovieDetails.setOnClickListener {
                getItem(position)?.let { currentItem -> recipeDetailsImpl.clickListenerMovieDetailView(currentItem) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MovieDetailViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    object DataDifferntiator : DiffUtil.ItemCallback<MovieDetails>() {

        override fun areItemsTheSame(oldItem: MovieDetails, newItem: MovieDetails): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieDetails, newItem: MovieDetails): Boolean {
            return oldItem == newItem
        }
    }

}
