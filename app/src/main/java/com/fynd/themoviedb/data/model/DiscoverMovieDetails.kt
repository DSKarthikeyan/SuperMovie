package com.fynd.themoviedb.data.model

import androidx.room.*
import com.fynd.themoviedb.data.db.Converters

@Entity(tableName = "discover-movie-details", indices = [Index(value = ["tableId"], unique = true)])
data class DiscoverMovieDetails(
    @PrimaryKey(autoGenerate = true)
    val tableId: Int?,
    val page: Int,
    var results: List<MovieDetails>,
    val total_pages: Int,
    val total_results: Int
)