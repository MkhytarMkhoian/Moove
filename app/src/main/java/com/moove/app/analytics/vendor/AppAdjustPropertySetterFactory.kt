package com.moove.app.analytics.vendor

import com.moove.app.analytics.property.PreferredSection
import io.github.mkhytarmkhoian.herald.Property
import io.github.mkhytarmkhoian.herald.Resolution
import io.github.mkhytarmkhoian.herald.adjust.AdjustPropertySetter
import io.github.mkhytarmkhoian.herald.adjust.AdjustPropertySetterFactory

/**
 * Adjust's property chain is strict, so the app module answers for its own property: the
 * preferred section is profile colour for Mixpanel, not an attribution signal, and is dropped
 * for Adjust on purpose rather than left to fall through as a failure.
 */
class AppAdjustPropertySetterFactory : AdjustPropertySetterFactory {

    override fun create(property: Property): Resolution<AdjustPropertySetter> = when (property) {
        is PreferredSection -> Resolution.Dropped
        else -> Resolution.Declined
    }
}
