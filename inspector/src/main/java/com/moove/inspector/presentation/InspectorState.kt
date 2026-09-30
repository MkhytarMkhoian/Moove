package com.moove.inspector.presentation

import android.os.Parcelable
import com.moove.inspector.presentation.model.InspectorEntryModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class InspectorState(
    val entries: List<InspectorEntryModel> = emptyList(),
) : Parcelable

sealed class InspectorEffect {
    data object GoBack : InspectorEffect()
}
