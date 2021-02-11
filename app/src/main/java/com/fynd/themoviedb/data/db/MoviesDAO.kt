package com.fynd.themoviedb.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fynd.themoviedb.data.model.DiscoverMovieDetails
import com.fynd.themoviedb.data.model.MovieDetails

@Dao
interface MoviesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(objects: List<MovieDetails?>)

    @Query("SELECT * FROM `movie-details`")
    fun getAllMovieDetails(): LiveData<List<MovieDetails>>

}