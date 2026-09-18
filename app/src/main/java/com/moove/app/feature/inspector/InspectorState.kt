package com.moove.app.feature.inspector

import android.os.Parcelable
import com.moove.app.feature.inspector.model.InspectorEntryModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class InspectorState(
    val entries: List<InspectorEntryModel> = emptyList(),
) : Parcelable

sealed class InspectorEffect {
    data object GoBack : InspectorEffect()
}
