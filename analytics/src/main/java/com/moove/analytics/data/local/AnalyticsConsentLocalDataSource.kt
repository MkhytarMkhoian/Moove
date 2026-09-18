package com.moove.analytics.data.local

import android.content.Context
import androidx.core.content.edit

internal class AnalyticsConsentLocalDataSource(context: Context) {

    private val prefs = context.getSharedPreferences("analytics", Context.MODE_PRIVATE)

    fun isEnabled(): Boolean = prefs.getBoolean(KEY_ENABLED, false)

    fun setEnabled(enabled: Boolean) = prefs.edit { putBoolean(KEY_ENABLED, enabled) }

    private companion object {
        const val KEY_ENABLED = "analytics_enabled"
    }
}
