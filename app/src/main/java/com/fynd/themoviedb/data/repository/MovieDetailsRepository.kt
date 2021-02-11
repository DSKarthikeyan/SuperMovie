package com.fynd.themoviedb.data.repository

import com.fynd.themoviedb.data.api.RetrofitApiInstance
import com.fynd.themoviedb.data.db.MovieDatabase
import com.fynd.themoviedb.data.model.MovieDetails

class MovieDetailsRepository(private val movieDetailsDatabase: MovieDatabase) {

    /**
     * fun: toGet Movie Details from server using Retrofit
     */
    suspend fun getDiscoveredMovieDetails() = RetrofitApiInstance.movieRepoApi.getDiscoveredMovieDetails()


    /**
     * fun: To Insert List of Repo details to Local DB
     */
    suspend fun upsert(reposDetails: List<MovieDetails>) =
        movieDetailsDatabase.getMovieDetailsDAO().upsert(reposDetails)

    /**
     * fun: toGet All Live Trending Repo Details from Local DB
     */
    fun getMovieDetailsFromLocal() = movieDetailsDatabase.getMovieDetailsDAO().getAllMovieDetails()


    /**
     * fun: To Insert List of Repo details to Local DB
     */
//    suspend fun insertCartList(cartList: CartList): Long =
//        recipeDatabase.getRecipeDetailsDAO().insertCartList(cartList)
//
//    fun getCartDetails() = recipeDatabase.getRecipeDetailsDAO().getAllCartList()
//
//    suspend fun getCartItemById(itemProductId: Int): CartList =
//        recipeDatabase.getRecipeDetailsDAO().getCartItemById(itemProductId)
//
//    fun deleteItemInCart(itemProductId: Int): Int =
//        recipeDatabase.getRecipeDetailsDAO().deleteItemInCart(itemProductId)
}