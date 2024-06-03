package com.moove.shared.feature.deeplink.presentation

import com.moove.shared.feature.deeplink.domain.DeepLink

interface DeepLinkNavigator {
    fun navigateTo(link: DeepLink)
}
