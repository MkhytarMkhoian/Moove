package com.moove.tickets.analytics.property

import io.github.mkhytarmkhoian.herald.AnalyticsValue
import io.github.mkhytarmkhoian.herald.Property

/**
 * Deliberately mapped for no vendor beyond the generic ones. Adjust's property chain is strict,
 * so setting this throws `UnhandledPropertyException` there, which Herald contains and reports —
 * the failure path, exercised on purpose.
 */
data class SeatPreference(val preference: String) : Property {
    override val name = "seat_preference"
    override val value = AnalyticsValue.String(preference)
}
