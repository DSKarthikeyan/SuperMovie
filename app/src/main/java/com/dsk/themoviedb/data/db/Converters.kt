package com.dsk.themoviedb.data.db

import androidx.room.TypeConverter
import com.dsk.themoviedb.data.model.MovieDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromString(value: String): List<Int> {
        val type = object: TypeToken<List<Int>>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromArrayList(list: List<Int>): String {
        val type = object: TypeToken<List<Int>>() {}.type
        return Gson().toJson(list, type)
    }

    //list of cutome object in your database
    @TypeConverter
    fun saveMovieDetails(listOfString: List<MovieDetails?>?): String? {
        return Gson().toJson(listOfString)
    }

    @TypeConverter
    fun getMovieDetails(listOfString: String?): List<MovieDetails?>? {
        return Gson().fromJson(
            listOfString,
            object : TypeToken<List<String?>?>() {}.type
        )
    }
}