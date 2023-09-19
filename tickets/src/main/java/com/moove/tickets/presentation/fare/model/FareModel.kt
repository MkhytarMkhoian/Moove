package com.moove.tickets.presentation.fare.model

import android.os.Parcelable
import com.moove.tickets.domain.model.Fare
import kotlinx.parcelize.Parcelize

@Parcelize
data class FareModel(
    val description: String,
    val price: Float,
) : Parcelable

fun Fare.asPresentation() = FareModel(
    description = description,
    price = price,
)

fun List<Fare>.asPresentation(): List<FareModel> = map { it.asPresentation() }

fun FareModel.asDomain() = Fare(
    description = description,
    price = price,
)
