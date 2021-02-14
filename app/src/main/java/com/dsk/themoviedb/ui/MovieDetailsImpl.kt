package com.dsk.themoviedb.ui

import com.dsk.themoviedb.data.model.MovieDetails

interface MovieDetailsImpl {
    fun clickListenerMovieDetailView(recipeDetails: MovieDetails) {}
}