package com.fynd.themoviedb.ui

import com.fynd.themoviedb.data.model.MovieDetails

interface MovieDetailsImpl {
    fun clickListenerMovieDetailView(recipeDetails: MovieDetails) {}
}