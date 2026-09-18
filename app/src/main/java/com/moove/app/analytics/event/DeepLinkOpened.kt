package com.moove.app.analytics.event

import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.parameters

data class DeepLinkOpened(val host: String, val path: String) : Event {
    override val name = "deep_link_opened"
    override val parameters = parameters {
        put("host", host)
        put("path", path)
    }
}
