package com.dsk.themoviedb.ui.movieDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dsk.themoviedb.MovieDetailsApplication
import com.dsk.themoviedb.data.repository.MovieDetailsRepository

class MovieDetailsVMProviderFactory(
    private val application: MovieDetailsApplication,
    private val movieRepository: MovieDetailsRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MovieDetailsViewModel(application,movieRepository) as T
    }
}