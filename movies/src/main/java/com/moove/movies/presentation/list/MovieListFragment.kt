package com.moove.movies.presentation.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.moove.movies.R
import com.moove.movies.databinding.MovieListFragmentBinding
import com.moove.movies.presentation.list.adapter.MovieListAdapter
import com.moove.movies.presentation.list.adapter.MoviesLoadStateAdapter
import com.moove.shared.presentation.fragment.delegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

@AndroidEntryPoint
class MovieListFragment : Fragment(R.layout.movie_list_fragment) {

    private val viewBinding by viewBinding(MovieListFragmentBinding::bind)
    private val viewModel: MovieListViewModel by viewModels()
    private val navigator: MovieListNavigator by inject {
        parametersOf(findNavController(), lifecycleScope, requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MovieListAdapter(onMovieClick = viewModel::onMovieClick)
        viewBinding.recyclerView.layoutManager = GridLayoutManager(requireContext(), GRID_SPAN)
        viewBinding.recyclerView.adapter = adapter.withLoadStateFooter(
            footer = MoviesLoadStateAdapter(onRetry = adapter::retry),
        )

        viewBinding.retryButton.setOnClickListener { adapter.refresh() }
        viewBinding.swipeRefresh.setOnRefreshListener { adapter.refresh() }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.movies.collect { pagingData -> adapter.submitData(pagingData) }
                }
                launch {
                    adapter.loadStateFlow.collect { states -> renderLoadState(adapter.itemCount, states.refresh) }
                }
                launch {
                    viewModel.container.sideEffectFlow.collect { effect -> handle(effect) }
                }
            }
        }
    }

    private fun renderLoadState(itemCount: Int, refresh: LoadState) {
        val listEmpty = itemCount == 0
        viewBinding.initialProgress.isVisible = refresh is LoadState.Loading && listEmpty
        viewBinding.errorContainer.isVisible = refresh is LoadState.Error && listEmpty
        viewBinding.swipeRefresh.isRefreshing = refresh is LoadState.Loading && !listEmpty
        if (refresh is LoadState.Error && listEmpty) {
            viewBinding.errorMessage.text = refresh.error.localizedMessage
        }
    }

    private fun handle(effect: MovieListEffect) {
        when (effect) {
            is MovieListEffect.GoToDetails -> navigator.goToDetails(effect.movieId)
            MovieListEffect.ShowGenericError ->
                Snackbar.make(viewBinding.root, R.string.movies_error_title, Snackbar.LENGTH_SHORT).show()
        }
    }

    private companion object {
        const val GRID_SPAN = 2
    }
}
