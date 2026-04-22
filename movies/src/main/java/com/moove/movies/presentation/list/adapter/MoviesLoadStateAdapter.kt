package com.moove.movies.presentation.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moove.movies.databinding.MovieListLoadStateBinding

internal class MoviesLoadStateAdapter(
    private val onRetry: () -> Unit,
) : LoadStateAdapter<MoviesLoadStateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val binding = MovieListLoadStateBinding.inflate(
            LayoutInflater.from(parent.context), parent, false,
        )
        return ViewHolder(binding, onRetry)
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    internal class ViewHolder(
        private val binding: MovieListLoadStateBinding,
        onRetry: () -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.loadStateRetry.setOnClickListener { onRetry() }
        }

        fun bind(loadState: LoadState) {
            binding.loadStateProgress.visibility = if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
            val isError = loadState is LoadState.Error
            binding.loadStateError.visibility = if (isError) View.VISIBLE else View.GONE
            binding.loadStateRetry.visibility = if (isError) View.VISIBLE else View.GONE
            if (loadState is LoadState.Error) {
                binding.loadStateError.text = loadState.error.localizedMessage
            }
        }
    }
}
