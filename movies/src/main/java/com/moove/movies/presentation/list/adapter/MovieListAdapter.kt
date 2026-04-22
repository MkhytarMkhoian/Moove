package com.moove.movies.presentation.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.moove.movies.databinding.MovieListItemBinding
import com.moove.movies.presentation.list.model.MovieSummaryModel

internal class MovieListAdapter(
    private val onMovieClick: (MovieSummaryModel) -> Unit,
) : PagingDataAdapter<MovieSummaryModel, MovieViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = MovieListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false,
        )
        return MovieViewHolder(binding, onMovieClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private companion object {
        val DIFF = object : DiffUtil.ItemCallback<MovieSummaryModel>() {
            override fun areItemsTheSame(old: MovieSummaryModel, new: MovieSummaryModel) =
                old.id == new.id

            override fun areContentsTheSame(old: MovieSummaryModel, new: MovieSummaryModel) =
                old == new
        }
    }
}
