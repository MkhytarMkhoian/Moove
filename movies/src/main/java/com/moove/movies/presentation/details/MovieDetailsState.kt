package com.moove.movies.presentation.details

import android.os.Parcelable
import com.moove.movies.presentation.details.model.MovieDetailsModel
import com.moove.shared.presentation.compose.component.ScreenContentStatus
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieDetailsState(
    val status: ScreenContentStatus = ScreenContentStatus.Idle,
    val details: MovieDetailsModel? = null,
) : Parcelable

sealed class MovieDetailsEffect {
    data object GoBack : MovieDetailsEffect()
    data object ShowGenericError : MovieDetailsEffect()
}
