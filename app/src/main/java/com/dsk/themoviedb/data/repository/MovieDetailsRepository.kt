package com.dsk.themoviedb.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.dsk.themoviedb.data.api.MovieRepoApi
import com.dsk.themoviedb.data.api.RetrofitApiInstance
import com.dsk.themoviedb.data.api.RetrofitApiInstance.Companion.movieRepoApi
import com.dsk.themoviedb.data.db.MovieDatabase
import com.dsk.themoviedb.data.model.MovieDetails
import com.dsk.themoviedb.data.repository.paging.PagingMovieMediator
import kotlinx.coroutines.flow.Flow

class MovieDetailsRepository(
    private val movieDetailsDatabase: MovieDatabase) {

    companion object {
        const val DEFAULT_PAGE_INDEX = 1
        const val DEFAULT_PAGE_SIZE = 500

        //get doggo repository instance
//        fun getInstance() = MovieDetailsRepository(movieDetailsDatabase)
    }

    /**
     * let's define page size, page size is the only required param, rest is optional
     */
    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(pageSize = DEFAULT_PAGE_SIZE, enablePlaceholders = true)
    }

    @ExperimentalPagingApi
    fun loadMovieDetailsDb(pagingConfig: PagingConfig = getDefaultPageConfig()): LiveData<PagingData<MovieDetails>> {
        if (movieDetailsDatabase == null) throw IllegalStateException("Database is not initialized")
        val pagingSourceFactory = { movieDetailsDatabase.getMovieDetailsDAO().getAllMovieDetails() }
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = PagingMovieMediator(movieRepoApi, movieDetailsDatabase)
        ).liveData
    }

    /**
     * fun: toGet Movie Details from server using Retrofit
     */
    suspend fun getDiscoveredMovieDetails(currentLoadingPageKey:Int) = RetrofitApiInstance.movieRepoApi.getDiscoveredMovieDetails(currentLoadingPageKey)


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