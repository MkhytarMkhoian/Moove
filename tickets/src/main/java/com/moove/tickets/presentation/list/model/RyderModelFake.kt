package com.moove.tickets.presentation.list.model

import com.moove.tickets.presentation.fare.model.fakeFareModels

internal val fakeRyderModels: List<RyderModel>
    get() = listOf(
        RyderModel(
            id = "Adult",
            fares = fakeFareModels,
            subtext = null,
        ),
        RyderModel(
            id = "Child",
            fares = fakeFareModels,
            subtext = "Ages 8-17",
        ),
        RyderModel(
            id = "Senior",
            fares = fakeFareModels,
            subtext = "Ages 60+",
        ),
    )