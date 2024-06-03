package com.moove.app.main

import android.os.Parcelable
import com.moove.shared.feature.deeplink.domain.DeepLink
import kotlinx.parcelize.Parcelize

@Parcelize
class MainActivityState : Parcelable

sealed class MainActivityEffect {
    data object ShowGenericError : MainActivityEffect()
    data class NavigateDeepLink(val deepLink: DeepLink) : MainActivityEffect()
}
