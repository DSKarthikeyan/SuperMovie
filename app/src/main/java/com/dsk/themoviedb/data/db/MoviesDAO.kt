package com.dsk.themoviedb.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dsk.themoviedb.data.model.MovieDetails

@Dao
interface MoviesDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(objects: List<MovieDetails?>)

//    @Query("SELECT * FROM `movie-details`")
//    fun getAllMovieDetails(): LiveData<List<MovieDetails>>

    @Query("SELECT * FROM 'movie-details'")
    fun getAllMovieDetails(): PagingSource<Int, MovieDetails>

    @Query("DELETE FROM 'movie-details'")
    suspend fun clearAllMovieDetails()


}