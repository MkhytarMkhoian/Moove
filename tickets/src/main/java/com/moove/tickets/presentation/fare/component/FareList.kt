package com.moove.tickets.presentation.fare.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.moove.design_system.compose.AppTheme
import com.moove.tickets.presentation.fare.model.FareModel
import com.moove.tickets.presentation.fare.model.fakeFareModels

@Composable
fun FareList(
    fares: List<FareModel>,
    onClick: (FareModel) -> Unit,
) {
    LazyColumn {
        items(fares) { item ->
            FareItem(fare = item, onClick = onClick)
        }
    }
}

@Preview(name = "Fare List", showBackground = true)
@Composable
private fun PreviewRyderList() {
    AppTheme {
        FareList(
            fares = fakeFareModels,
            onClick = {}
        )
    }
}
