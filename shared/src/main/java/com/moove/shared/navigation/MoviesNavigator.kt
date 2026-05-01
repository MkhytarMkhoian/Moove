package com.moove.shared.navigation

interface MoviesNavigator {

    fun goToMovieList()
    fun goToMovieDetails(movieId: Long)
}
