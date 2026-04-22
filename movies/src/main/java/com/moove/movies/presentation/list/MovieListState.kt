package com.moove.movies.presentation.list

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class MovieListState : Parcelable

sealed class MovieListEffect {
    data class GoToDetails(val movieId: Long) : MovieListEffect()
    data object ShowGenericError : MovieListEffect()
}
