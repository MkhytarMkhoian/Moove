package com.moove.tickets.presentation.fare.model

internal val fakeFareModels: List<FareModel>
    get() = listOf(
        FareModel(
            description = "2.5 Hour Ticket",
            price = 2.5f,
        ),
        FareModel(
            description = "1 Day Pass",
            price = 5.0f,
        ),
        FareModel(
            description = "30 Day Pass",
            price = 100f,
        )
    )