package com.moove.tickets.analytics.vendor

import com.adjust.sdk.AdjustInstance
import com.moove.tickets.analytics.property.TicketsPurchased
import io.github.mkhytarmkhoian.herald.Property
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.adjust.AdjustPropertySetter
import io.github.mkhytarmkhoian.herald.adjust.AdjustPropertySetterFactory
import io.github.mkhytarmkhoian.herald.adjust.setters.GenericPropertySetter

/**
 * Adjust's property chain is strict, so every property that may reach it is named here. The
 * setter itself is Herald's: a global callback parameter is all Adjust can do with a property.
 * `SeatPreference` is not named, on purpose; see its KDoc.
 */
class TicketsAdjustPropertySetterFactory(
    private val adjust: AdjustInstance,
) : AdjustPropertySetterFactory {

    override fun create(property: Property): Resolution<AdjustPropertySetter> = when (property) {
        is TicketsPurchased -> Resolution.Claimed(GenericPropertySetter(property, adjust))
        else -> Resolution.Declined
    }
}
