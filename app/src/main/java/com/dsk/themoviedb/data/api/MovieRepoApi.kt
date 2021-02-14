package com.dsk.themoviedb.data.api

import com.dsk.themoviedb.data.model.DiscoverMovieDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieRepoApi {

    @GET("/3/discover/movie")
    suspend fun getDiscoveredMovieDetails(@Query("page") pageNumber: Int): Response<DiscoverMovieDetails>
}