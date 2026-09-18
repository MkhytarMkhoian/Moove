package com.moove.tickets.analytics.event

import io.github.mkhytarmkhoian.herald.ScreenViewEvent

data object RyderListScreenViewed : ScreenViewEvent {
    override val name = "ryder_list_shown"
    override val screenName = "RyderList"
}
