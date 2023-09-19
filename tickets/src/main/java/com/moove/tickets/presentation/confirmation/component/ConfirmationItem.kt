package com.moove.tickets.presentation.confirmation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.moove.design_system.compose.AppTheme
import com.moove.design_system.compose.Button
import com.moove.design_system.compose.Spacing
import com.moove.tickets.presentation.fare.model.FareModel
import com.moove.tickets.presentation.fare.model.fakeFareModels

@Composable
fun ConfirmationItem(
    modifier: Modifier = Modifier,
    ryderId: String,
    fare: FareModel,
    ticketCount: Int,
    onIncrementTicketClick: () -> Unit,
    onDecrementTicketClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Spacing.S, vertical = Spacing.S),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = modifier.weight(1f)
        ) {
            Text(
                text = ryderId,
                style = AppTheme.typography.material.h1,
                maxLines = 1,
            )
            Text(
                text = fare.description,
                style = AppTheme.typography.material.subtitle1,
                maxLines = 1,
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onIncrementTicketClick,
                content = { Text(text = "+") }
            )
            Text(
                text = ticketCount.toString(),
                style = AppTheme.typography.material.h3,
                maxLines = 1,
            )
            Button(
                onClick = onDecrementTicketClick,
                content = { Text(text = "-") }
            )
        }
    }
}

@Preview(name = "Confirmation Item", showBackground = true)
@Composable
private fun PreviewConfirmationItem() {
    AppTheme {
        ConfirmationItem(
            ryderId = "Adult",
            fare = fakeFareModels.first(),
            ticketCount = 2,
            onIncrementTicketClick = {},
            onDecrementTicketClick = {}
        )
    }
}
