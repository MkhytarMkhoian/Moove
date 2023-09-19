package com.moove.tickets.presentation.confirmation

import android.os.Parcelable
import com.moove.shared.presentation.compose.component.ScreenContentStatus
import com.moove.tickets.presentation.fare.model.FareModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConfirmationState(
    val status: ScreenContentStatus = ScreenContentStatus.Idle,
    val ryderId: String,
    val fare: FareModel,
    val ticketCount: Int,
    val totalPrice: Float,
) : Parcelable

sealed class ConfirmationEffect {
    object ShowGenericError : ConfirmationEffect()
    object ShowSuccessMessage : ConfirmationEffect()
}