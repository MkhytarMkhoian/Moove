package com.moove.app.feature.inspector.model

internal val fakeInspectorEntryModels = listOf(
    InspectorEntryModel(
        at = 1_700_000_003_000L,
        summary = "FAILED adjust on SetProperty(propertyName=seat_preference): no factory claimed it",
        failed = true
    ),
    InspectorEntryModel(
        at = 1_700_000_002_000L,
        summary = "property seat_preference = window",
        failed = false
    ),
    InspectorEntryModel(
        at = 1_700_000_001_000L,
        summary = "event ticket_purchased { count = 2, total = 9.98 }",
        failed = false
    ),
    InspectorEntryModel(at = 1_700_000_000_000L, summary = "start", failed = false),
)
