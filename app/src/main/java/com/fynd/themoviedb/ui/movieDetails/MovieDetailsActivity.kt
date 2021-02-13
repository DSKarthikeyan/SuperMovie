package com.fynd.themoviedb.ui.movieDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fynd.themoviedb.MovieDetailsApplication
import com.fynd.themoviedb.R
import com.fynd.themoviedb.adapter.LoadStateAdapterView
import com.fynd.themoviedb.adapter.MainListAdapter
import com.fynd.themoviedb.adapter.MovieDetailsAdapter
import com.fynd.themoviedb.data.db.MovieDatabase
import com.fynd.themoviedb.data.model.MovieDetails
import com.fynd.themoviedb.data.repository.MovieDetailsRepository
import com.fynd.themoviedb.databinding.ActivityMovieDetailsBinding
import com.fynd.themoviedb.databinding.MovieDetailViewBinding
import com.fynd.themoviedb.ui.MovieDetailsImpl
import com.fynd.themoviedb.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MovieDetailsActivity : AppCompatActivity(), MovieDetailsImpl {

    lateinit var movieDetailsViewModel: MovieDetailsViewModel
    lateinit var movieDetailsListAdapter: MovieDetailsAdapter
    lateinit var mainListAdapter: MainListAdapter

    private lateinit var binding: ActivityMovieDetailsBinding

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
        movieDetailsViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(MovieDetailsViewModel::class.java)

        initTrendingRepoView()

//        buttonTryAgain.setOnClickListener { recipeViewModel.getRecipes() }
    }

    /**
     * fun: initialize and load Repo Details View
     */
    private fun initTrendingRepoView() {
        setupRecyclerView()
        movieDetailsViewModel.listData.observe(this@MovieDetailsActivity) {
            GlobalScope.launch(Dispatchers.Main) { mainListAdapter.submitData(it) }
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
        mainListAdapter = MainListAdapter(this)
        binding.recyclerViewMovieDetails.apply {
            adapter = mainListAdapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapterView { mainListAdapter.retry() },
                footer = LoadStateAdapterView { mainListAdapter.retry() }
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