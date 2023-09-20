package com.moove.tickets.domain.model

import com.moove.shared.faker
import com.moove.tickets.presentation.fare.model.FareModel
import com.moove.tickets.presentation.list.model.RyderModel

fun randomFare() = Fare(
    description = faker.name().fullName(),
    price = faker.number().randomDigitNotZero().toFloat(),
)

fun randomRyder() = Ryder(
    id = faker.idNumber().valid(),
    subtext = faker.name().fullName(),
    fares = listOf(randomFare(), randomFare()),
)
