package com.dsk.themoviedb.adapter.movieDetailsAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dsk.themoviedb.R
import com.dsk.themoviedb.data.model.MovieView
import com.dsk.themoviedb.databinding.ItemSeperatorViewBinding
import com.dsk.themoviedb.databinding.MovieDetailViewBinding
import com.dsk.themoviedb.ui.MovieDetailsImpl
import com.dsk.themoviedb.util.APIConstants

class MovieDetailsPagingAdapterSeparator(private val recipeDetailsImpl: MovieDetailsImpl) :
    PagingDataAdapter<MovieView, RecyclerView.ViewHolder>(
        MovieModelComparator
    ) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movieModel: MovieView = getItem(position)!!
        movieModel.let {
            when (movieModel) {
                is MovieView.MovieItem -> {
                    val itemViewHolder = holder as ViewHolder
                    Glide.with(holder.itemView)
                        .asBitmap()
                        .load("${APIConstants.BASE_IMAGE_URL_w500_API}${movieModel.movie.poster_path}")
                        .into(itemViewHolder.viewBinding.imageViewMovie)
                    itemViewHolder.viewBinding.textViewMovieTitle.text =
                        movieModel.movie.title
                    itemViewHolder.viewBinding.cardViewMovieDetails.setOnClickListener {
                        movieModel.movie.let { currentItem ->
                            recipeDetailsImpl.clickListenerMovieDetailView(
                                currentItem
                            )
                        }
                    }
                }
                is MovieView.SeparatorItem -> {
                    val viewHolder = holder as MovieSeparatorViewHolder
                    viewHolder.itemSeparatorViewBinding.textViewSeperator.text = movieModel.Language
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MovieView.MovieItem -> R.layout.movie_detail_view
            is MovieView.SeparatorItem -> R.layout.item_seperator_view
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.movie_detail_view -> {
                val binding = MovieDetailViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ViewHolder(binding)
            }
            else -> {
                val binding = ItemSeperatorViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MovieSeparatorViewHolder(binding)
            }
        }
    }

    class ViewHolder(var viewBinding: MovieDetailViewBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    class MovieSeparatorViewHolder(var itemSeparatorViewBinding: ItemSeperatorViewBinding) :
        RecyclerView.ViewHolder(itemSeparatorViewBinding.root)

    companion object {
        private val MovieModelComparator = object : DiffUtil.ItemCallback<MovieView>() {
            override fun areItemsTheSame(oldItem: MovieView, newItem: MovieView): Boolean {
                return (oldItem is MovieView.MovieItem && newItem is MovieView.MovieItem &&
                        oldItem.movie.id == newItem.movie.id) ||
                        (oldItem is MovieView.SeparatorItem && newItem is MovieView.SeparatorItem &&
                                oldItem.Language == newItem.Language)
            }

            override fun areContentsTheSame(oldItem: MovieView, newItem: MovieView): Boolean =
                oldItem == newItem
        }
    }

}