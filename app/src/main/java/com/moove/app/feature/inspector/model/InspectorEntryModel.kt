package com.moove.app.feature.inspector.model

import android.os.Parcelable
import com.moove.analytics.RecentEventsProvider.Entry
import kotlinx.parcelize.Parcelize

@Parcelize
data class InspectorEntryModel(
    val at: Long,
    val summary: String,
    val failed: Boolean,
) : Parcelable

fun Entry.asPresentation() = InspectorEntryModel(
    at = at,
    summary = summary,
    failed = this is Entry.Failed,
)

fun List<Entry>.asPresentation() = map { it.asPresentation() }
