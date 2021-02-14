package com.dsk.themoviedb.data.model

import androidx.room.*

@Entity(tableName = "discover-movie-details", indices = [Index(value = ["tableId"], unique = true)])
data class DiscoverMovieDetails(
    @PrimaryKey(autoGenerate = true)
    val tableId: Int?,
    val page: Int,
    var results: List<MovieDetails>,
    val total_pages: Int,
    val total_results: Int
)