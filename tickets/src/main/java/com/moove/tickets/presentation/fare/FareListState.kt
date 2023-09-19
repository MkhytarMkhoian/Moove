package com.moove.tickets.presentation.fare

import android.os.Parcelable
import com.moove.shared.presentation.compose.component.ScreenContentStatus
import com.moove.tickets.presentation.fare.model.FareModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class FareListState(
    val status: ScreenContentStatus = ScreenContentStatus.Idle,
    val fares: List<FareModel> = emptyList(),
) : Parcelable

sealed class FareListEffect {
    data class GoToConfirmation(val ryderId: String, val fare: FareModel) : FareListEffect()
    object ShowGenericError : FareListEffect()
}