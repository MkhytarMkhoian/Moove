package com.moove.app.feature.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeState(
    val analyticsEnabled: Boolean = false,
    val signedInUserId: String? = null,
) : Parcelable

sealed class HomeEffect {
    data object GoToRyderList : HomeEffect()
    data object GoToMovieList : HomeEffect()
    data object GoToInspector : HomeEffect()
}
