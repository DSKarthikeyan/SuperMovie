package com.dsk.themoviedb.ui.movieDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.dsk.themoviedb.MovieDetailsApplication
import com.dsk.themoviedb.R
import com.dsk.themoviedb.adapter.LoadStateAdapterView
import com.dsk.themoviedb.adapter.movieDetailsAdapter.MovieDetailsPagingAdapter
import com.dsk.themoviedb.adapter.movieDetailsAdapter.MovieDetailsNormalAdapter
import com.dsk.themoviedb.data.db.MovieDatabase
import com.dsk.themoviedb.data.model.MovieDetails
import com.dsk.themoviedb.data.repository.MovieDetailsRepository
import com.dsk.themoviedb.databinding.ActivityMovieDetailsBinding
import com.dsk.themoviedb.ui.MovieDetailsImpl
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter

@ExperimentalPagingApi
@InternalCoroutinesApi
class MovieDetailsActivity : AppCompatActivity(), MovieDetailsImpl {

    lateinit var movieDetailsViewModel: MovieDetailsViewModel
    lateinit var movieDetailsListNormalAdapter: MovieDetailsNormalAdapter
    lateinit var movieDetailsPagingAdapter: MovieDetailsPagingAdapter

    private lateinit var binding: ActivityMovieDetailsBinding
    private var coroutineJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initializeViews()
    }

    /**
     * fun: initialize View Objects
     */
    private fun initializeViews() {
        val movieDetailsDatabase = MovieDatabase(this)
        val recipeRepository = MovieDetailsRepository(movieDetailsDatabase)
        val viewModelProviderFactory = MovieDetailsVMProviderFactory(
            application as MovieDetailsApplication,
            recipeRepository
        )
        movieDetailsViewModel = ViewModelProvider(this, viewModelProviderFactory).get(MovieDetailsViewModel::class.java)

        lifecycleScope.launch {
            initTrendingRepoView()
        }

//        buttonTryAgain.setOnClickListener { recipeViewModel.getRecipes() }
    }

    /**
     * fun: initialize and load Repo Details View
     */
    private suspend fun initTrendingRepoView() {
        setupRecyclerView()
        movieDetailsViewModel.listData.observe(this@MovieDetailsActivity) {
            coroutineJob?.cancel()
            coroutineJob = lifecycleScope.launch {
                movieDetailsPagingAdapter.submitData(it)
            }
        }

    }

    /**
     * fun: Hide loading Progress Bar
     */
    private fun hideProgressBar() {
        binding.recyclerViewMovieDetails.visibility = View.VISIBLE
        binding.progressCircularBar.visibility = View.INVISIBLE
        binding.textViewStatus.visibility = View.INVISIBLE
//        buttonTryAgain.visibility = View.INVISIBLE
    }

    /**
     * fun: show progress bar
     */
    private fun showProgressBar() {
        binding.recyclerViewMovieDetails.visibility = View.INVISIBLE
        binding.progressCircularBar.visibility = View.VISIBLE
        showLoadingText(resources.getString(R.string.text_loading))
//        buttonTryAgain.visibility = View.INVISIBLE
    }

    /**
     * fun: ShowText based on Loader
     */
    private fun showLoadingText(message: String) {
        if (message.isNotEmpty()) {
            binding.recyclerViewMovieDetails.visibility = View.INVISIBLE
            binding.textViewStatus.visibility = View.VISIBLE
            binding.textViewStatus.text = message
//            buttonTryAgain.visibility = View.VISIBLE
        }
    }

    /**
     * fun: setup Recycler View
     */
    private fun setupRecyclerView() {
        movieDetailsPagingAdapter = MovieDetailsPagingAdapter(this)
        binding.recyclerViewMovieDetails.apply {
            adapter = movieDetailsPagingAdapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapterView { movieDetailsPagingAdapter.retry() },
                footer = LoadStateAdapterView { movieDetailsPagingAdapter.retry() }
            )
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@MovieDetailsActivity, 2)
        }
    }

    override fun clickListenerMovieDetailView(recipeDetails: MovieDetails) {
        GlobalScope.launch(Dispatchers.Main) {
            movieDetailsViewModel.apply {

            }
        }
    }

//    /** Called when the user taps the Send button */
//    private fun openCartDetailsActivity() {
//        val intent = Intent(this, CartDetailsActivity::class.java)
//        startActivity(intent)
//        finish()
//    }

}