package com.moove.tickets.analytics.event

import io.github.mkhytarmkhoian.herald.Event

/**
 * A marker of Moove's own: the user chose a piece of content. Firebase reports it as GA4's
 * `select_content`; the other vendors see a plain event. It lives here rather than in Herald
 * because which interactions count as "selecting content" is this app's convention.
 */
interface ContentEvent : Event {
    val contentType: String
    val itemId: String
}
