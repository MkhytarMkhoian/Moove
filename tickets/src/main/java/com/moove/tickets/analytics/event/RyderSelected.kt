package com.moove.tickets.analytics.event

import io.github.mkhytarmkhoian.herald.parameters

data class RyderSelected(val ryderId: String) : ContentEvent {
    override val name = "ryder_selected"
    override val contentType = "ryder"
    override val itemId = ryderId
    override val parameters = parameters { put("ryder_id", ryderId) }
}
