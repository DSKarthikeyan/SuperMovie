package com.dsk.themoviedb.data.model

// Model class to Display Movie Details with Separator in Movie Details Activity based on Language
sealed class MovieView {
    data class MovieItem(val movie: MovieDetails) : MovieView()
    data class SeparatorItem(val Language: String) : MovieView()
}
