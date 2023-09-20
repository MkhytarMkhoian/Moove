package com.moove.tickets.data.dto

import com.moove.shared.faker
import com.moove.tickets.data.local.dto.FareDTO
import com.moove.tickets.data.local.dto.RyderDTO

fun randomFareDTO() = FareDTO(
    description = faker.name().fullName(),
    price = faker.number().randomDigitNotZero().toFloat(),
)

fun randomRyderDTO() = RyderDTO(
    fares = listOf(randomFareDTO(), randomFareDTO()),
    subtext = faker.name().fullName(),
)
