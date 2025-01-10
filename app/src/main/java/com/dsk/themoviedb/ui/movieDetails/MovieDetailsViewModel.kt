package com.dsk.themoviedb.ui.movieDetails

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.*
import com.dsk.themoviedb.MovieDetailsApplication
import com.dsk.themoviedb.data.model.DiscoverMovieDetails
import com.dsk.themoviedb.data.model.MovieDetails
import com.dsk.themoviedb.data.repository.MovieDetailsRepository
import com.dsk.themoviedb.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

@ExperimentalPagingApi
class MovieDetailsViewModel(
    application: MovieDetailsApplication,
    private val recipeRepository: MovieDetailsRepository
) : AndroidViewModel(application) {

    private val movieDetailsList: MutableLiveData<Resource<List<MovieDetails>>> = MutableLiveData()

    private var listMovieMediatorData: LiveData<PagingData<MovieDetails>> = fetchMovieDetails()

     fun fetchMovieDetails(): LiveData<PagingData<MovieDetails>> {
        val newResult: LiveData<PagingData<MovieDetails>> = recipeRepository.loadMovieDetailsDb()
            .cachedIn(viewModelScope)
        listMovieMediatorData = newResult
        return newResult
    }

    /**
     * fun: to get Trending Repo details from server
     */
    fun getRecipes() = viewModelScope.launch {
        getRecipesFromServer()
    }

    /**
     * fun: get trending repo from local database through repository
     */
    fun getRecipesFromLocal() = recipeRepository.getMovieDetailsFromLocal()

    /**
     * fun: get trending repo from server
     */
    private suspend fun getRecipesFromServer() {
        try {
            if (hasInternetConnection()) {
                movieDetailsList.postValue(Resource.Loading())
                val response = recipeRepository.getDiscoveredMovieDetails(1)
                Log.d("DSK ", "response $response")
                movieDetailsList.postValue(handleRecipeResponse(response))
            } else {
                movieDetailsList.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> movieDetailsList.postValue(Resource.Error("No Internet Connection"))
                else -> movieDetailsList.postValue(Resource.Error("Conversion Error ${t.localizedMessage}"))
            }
        }
    }

    /**
     * fun: toHandle RepoDetails input and insert to LocalDB
     *    and to return if Server fetched response is successful
     */
    private fun handleRecipeResponse(response: Response<DiscoverMovieDetails>): Resource<List<MovieDetails>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                viewModelScope.launch(Dispatchers.IO) {
                    recipeRepository.upsert(resultResponse.results)
                }
                return Resource.Success(resultResponse.results)
            }
        }
        return Resource.Error(response.message())
    }

    /**
     * fun: to handle Internet Connectivity
     */
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MovieDetailsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}