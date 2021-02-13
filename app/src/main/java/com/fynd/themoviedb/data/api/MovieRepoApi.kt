package com.fynd.themoviedb.data.api

import com.fynd.themoviedb.data.model.DiscoverMovieDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieRepoApi {

    @GET("/3/discover/movie")
    suspend fun getDiscoveredMovieDetails(@Query("page") pageNumber: Int): Response<DiscoverMovieDetails>
}