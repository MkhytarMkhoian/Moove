package com.moove.movies.presentation.list.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.moove.movies.R
import com.moove.movies.databinding.MovieListItemBinding
import com.moove.movies.presentation.list.model.MovieSummaryModel

internal class MovieViewHolder(
    private val binding: MovieListItemBinding,
    onMovieClick: (MovieSummaryModel) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private var currentMovie: MovieSummaryModel? = null

    init {
        binding.root.setOnClickListener {
            currentMovie?.let(onMovieClick)
        }
    }

    fun bind(movie: MovieSummaryModel?) {
        currentMovie = movie
        if (movie == null) {
            binding.poster.setImageResource(R.drawable.ic_poster_placeholder)
            binding.title.text = null
            binding.rating.text = null
            binding.year.text = null
            return
        }
        binding.title.text = movie.title
        binding.rating.text = binding.root.context.getString(R.string.movies_rating_format, movie.rating)
        binding.year.text = movie.releaseYear.orEmpty()
        binding.poster.contentDescription = binding.root.context.getString(
            R.string.movie_poster_content_description,
            movie.title,
        )
        binding.poster.load(movie.posterUrl()) {
            placeholder(R.drawable.ic_poster_placeholder)
            error(R.drawable.ic_poster_placeholder)
            crossfade(true)
        }
    }
}
