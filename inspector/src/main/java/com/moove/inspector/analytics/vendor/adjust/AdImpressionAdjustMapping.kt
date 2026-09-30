package com.moove.inspector.analytics.vendor.adjust

import com.moove.inspector.analytics.event.AdImpressionSimulated
import io.github.mkhytarmkhoian.herald.adjust.AdjustAdRevenueEvent

internal fun AdImpressionSimulated.toAdjustAdRevenueEvent() = AdjustAdRevenueEvent(
    name = name,
    source = "applovin_max_sdk",
    revenue = revenue,
    currency = currency,
    adRevenueNetwork = network,
    adRevenueUnit = adUnit,
    adImpressionsCount = 1,
    parameters = parameters,
)
