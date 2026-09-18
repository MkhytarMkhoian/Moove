package com.moove.app.analytics.event

import io.github.mkhytarmkhoian.herald.adjust.AdRevenueEvent

/**
 * Moove shows no ads; the inspector fires this so the ad-revenue path can be seen end to end.
 * Adjust sends it through `trackAdRevenue`; the other vendors see a plain event.
 */
data class AdImpressionSimulated(override val revenue: Double) : AdRevenueEvent {
    override val name = "ad_impression"
    override val source = "applovin_max_sdk"
    override val currency = "USD"
    override val adRevenueNetwork = "Demo Network"
    override val adRevenueUnit = "home_banner"
    override val adImpressionsCount = 1
}
