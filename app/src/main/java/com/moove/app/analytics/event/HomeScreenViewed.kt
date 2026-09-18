package com.moove.app.analytics.event

import io.github.mkhytarmkhoian.herald.ScreenViewEvent

data object HomeScreenViewed : ScreenViewEvent {
    override val name = "home_shown"
    override val screenName = "Home"
}
