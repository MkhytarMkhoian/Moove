package com.moove.inspector.analytics.vendor.appsflyer

import com.appsflyer.share.MediationNetwork
import com.moove.inspector.analytics.event.AdImpressionSimulated
import io.github.mkhytarmkhoian.herald.appsflyer.AppsFlyerAdRevenueEvent

internal fun AdImpressionSimulated.toAppsFlyerAdRevenueEvent() = AppsFlyerAdRevenueEvent(
    name = name,
    monetizationNetwork = network,
    mediationNetwork = MediationNetwork.APPLOVIN_MAX,
    revenue = revenue,
    currency = currency,
    parameters = parameters,
)
