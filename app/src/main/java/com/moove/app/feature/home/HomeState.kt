package com.moove.app.feature.home

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class HomeState : Parcelable

sealed class HomeEffect {
    data object GoToRyderList : HomeEffect()
}
