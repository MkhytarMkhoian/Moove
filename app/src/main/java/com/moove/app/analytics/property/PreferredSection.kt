package com.moove.app.analytics.property

import io.github.mkhytarmkhoian.herald.AnalyticsValue
import io.github.mkhytarmkhoian.herald.UserProperty

/** The section the user went to last. A profile attribute, so Mixpanel keeps it on the person. */
data class PreferredSection(val section: String) : UserProperty {
    override val name = "preferred_section"
    override val value = AnalyticsValue.String(section)
}
