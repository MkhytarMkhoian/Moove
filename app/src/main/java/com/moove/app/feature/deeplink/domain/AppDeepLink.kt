package com.moove.app.feature.deeplink.domain

import com.moove.shared.feature.deeplink.domain.DeepLink
import com.moove.tickets.domain.model.Fare

sealed class AppDeepLink: DeepLink {
    data object Unknown : AppDeepLink()
    data object Home : AppDeepLink()
    data class FareList(val ryderId: String) : AppDeepLink()

    data class Confirmation(
        val ryderId: String,
        val fare: Fare,
    ) : AppDeepLink()
}
