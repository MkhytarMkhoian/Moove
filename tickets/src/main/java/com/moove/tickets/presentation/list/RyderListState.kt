package com.moove.tickets.presentation.list

import android.os.Parcelable
import com.moove.shared.presentation.compose.component.ScreenContentStatus
import com.moove.tickets.presentation.list.model.RyderModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class RyderListState(
    val status: ScreenContentStatus = ScreenContentStatus.Idle,
    val ryders: List<RyderModel> = emptyList(),
) : Parcelable

sealed class RyderListEffect {
    data class GoToFares(val id: String) : RyderListEffect()
    object ShowGenericError : RyderListEffect()
}