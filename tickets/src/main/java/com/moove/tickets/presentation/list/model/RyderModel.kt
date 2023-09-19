package com.moove.tickets.presentation.list.model

import android.os.Parcelable
import com.moove.tickets.domain.model.Ryder
import com.moove.tickets.presentation.fare.model.FareModel
import com.moove.tickets.presentation.fare.model.asPresentation
import kotlinx.parcelize.Parcelize

@Parcelize
data class RyderModel(
    val id: String,
    val fares: List<FareModel>,
    val subtext: String?,
) : Parcelable

fun Ryder.asPresentation() = RyderModel(
    id = id,
    fares = fares.asPresentation(),
    subtext = subtext,
)

fun List<Ryder>.asPresentation(): List<RyderModel> = map { it.asPresentation() }
