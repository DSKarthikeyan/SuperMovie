package com.dsk.themoviedb.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dsk.themoviedb.data.model.DiscoverMovieDetails
import com.dsk.themoviedb.data.model.MovieDetails
import com.dsk.themoviedb.data.model.RemoteKeys

@Database(
    entities = [DiscoverMovieDetails::class, MovieDetails::class,RemoteKeys::class],
    version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun getMovieDetailsDAO(): MoviesDAO
    abstract fun getRemoteKeysDAO(): RemoteKeysDAO

    companion object {
        @Volatile
        private var instance: MovieDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MovieDatabase::class.java,
                "movie-details.db"
            ).build()
    }


}