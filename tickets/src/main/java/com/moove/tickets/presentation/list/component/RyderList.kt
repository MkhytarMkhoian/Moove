package com.moove.tickets.presentation.list.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.moove.design_system.compose.AppTheme
import com.moove.tickets.presentation.list.model.RyderModel
import com.moove.tickets.presentation.list.model.fakeRyderModels

@Composable
fun RyderList(
    ryders: List<RyderModel>,
    onClick: (RyderModel) -> Unit,
) {
    LazyColumn {
        items(ryders) { item ->
            RyderItem(ryder = item, onClick = onClick)
        }
    }
}

@Preview(name = "Ryder List", showBackground = true)
@Composable
private fun PreviewRyderList() {
    AppTheme {
        RyderList(
            ryders = fakeRyderModels,
            onClick = {}
        )
    }
}
