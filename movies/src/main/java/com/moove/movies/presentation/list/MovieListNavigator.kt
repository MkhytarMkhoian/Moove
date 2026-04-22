package com.moove.movies.presentation.list

import com.moove.shared.navigation.MoviesNavigator
import com.moove.shared.navigation.ScreenNavigator

class MovieListNavigator(
    private val moviesNavigator: MoviesNavigator,
    private val screenNavigator: ScreenNavigator,
) : ScreenNavigator {

    fun goToDetails(movieId: Long) {
        moviesNavigator.goToMovieDetails(movieId)
    }

    override fun goBack() {
        screenNavigator.goBack()
    }
}
