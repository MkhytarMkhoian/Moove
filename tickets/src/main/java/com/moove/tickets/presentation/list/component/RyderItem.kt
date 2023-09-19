package com.moove.tickets.presentation.list.component

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
import com.moove.tickets.presentation.list.model.RyderModel
import com.moove.tickets.presentation.list.model.fakeRyderModels

@Composable
fun RyderItem(
    modifier: Modifier = Modifier,
    ryder: RyderModel,
    onClick: (RyderModel) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = { onClick(ryder) })
            .padding(horizontal = Spacing.S, vertical = Spacing.S)
    ) {
        Text(
            text = ryder.id,
            style = AppTheme.typography.material.h1,
            maxLines = 1,
        )
        ryder.subtext?.let {
            Text(
                text = ryder.subtext,
                style = AppTheme.typography.material.subtitle1,
                maxLines = 1,
            )
        }
    }
}

@Preview(name = "Ryder Item", showBackground = true)
@Composable
private fun PreviewRyderItem() {
    AppTheme {
        RyderItem(
            ryder = fakeRyderModels[1],
            onClick = {}
        )
    }
}
