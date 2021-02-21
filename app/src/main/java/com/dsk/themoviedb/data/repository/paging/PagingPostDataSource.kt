package com.dsk.themoviedb.data.repository.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dsk.themoviedb.data.model.MovieDetails
import com.dsk.themoviedb.data.repository.MovieDetailsRepository
import com.dsk.themoviedb.util.Constants
import okio.IOException
import retrofit2.HttpException

class PagingPostDataSource(
    private val recipeRepository: MovieDetailsRepository
) : PagingSource<Int, MovieDetails>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDetails> {
        try {
            val currentLoadingPageKey = params.key ?: Constants.DEFAULT_PAGE_INDEX
            val response = recipeRepository.getDiscoveredMovieDetails(currentLoadingPageKey)
            val responseData = mutableListOf<MovieDetails>()
            val data = response.body()?.results ?: emptyList()
            responseData.addAll(data)

            val prevKey = if (currentLoadingPageKey <= Constants.DEFAULT_PAGE_INDEX) null else currentLoadingPageKey - 1

            return LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = currentLoadingPageKey.plus(1)
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    // Item-keyed.
    override fun getRefreshKey(state: PagingState<Int, MovieDetails>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestItemToPosition(anchorPosition)?.id
        }
    }

//    // Positional.
//    override fun getRefreshKey(state: PagingState): Int? {
//        return state.anchorPosition
//    }
}