package com.moove.inspector.analytics.event

import io.github.mkhytarmkhoian.herald.Event

/**
 * Moove shows no ads; the inspector fires this so the ad-revenue path can be seen end to end.
 */
data class AdImpressionSimulated(
    val revenue: Double,
    val currency: String = "USD",
    val network: String = "Demo Network",
    val adUnit: String = "home_banner",
) : Event {
    override val name = "ad_impression"
}
