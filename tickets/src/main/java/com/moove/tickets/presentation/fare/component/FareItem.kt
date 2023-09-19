package com.moove.tickets.presentation.fare.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.moove.design_system.compose.AppTheme
import com.moove.design_system.compose.Spacing
import com.moove.tickets.presentation.fare.model.FareModel
import com.moove.tickets.presentation.fare.model.fakeFareModels

@Composable
fun FareItem(
    modifier: Modifier = Modifier,
    fare: FareModel,
    onClick: (FareModel) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick(fare) })
            .padding(horizontal = Spacing.S, vertical = Spacing.S)
    ) {
        Text(
            text = fare.description,
            style = AppTheme.typography.material.h1,
            maxLines = 1,
        )
        Text(
            text = "$".plus(String.format("%.2f", fare.price)),
            style = AppTheme.typography.material.subtitle1,
            maxLines = 1,
        )
    }
}

@Preview(name = "Fare Item", showBackground = true)
@Composable
private fun PreviewFareItem() {
    AppTheme {
        FareItem(
            fare = fakeFareModels.first(),
            onClick = {}
        )
    }
}
