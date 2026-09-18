package com.moove.app.analytics.event

import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.parameters

data class SectionSelected(val section: String) : Event {
    override val name = "section_selected"
    override val parameters = parameters { put("section", section) }
}
