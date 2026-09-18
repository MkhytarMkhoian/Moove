package com.moove.app.feature.inspector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moove.app.feature.inspector.model.InspectorEntryModel
import com.moove.app.feature.inspector.model.fakeInspectorEntryModels
import com.moove.design_system.compose.AppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun InspectorScreen(
    uiState: InspectorState,
    onBack: () -> Unit,
    onSimulateAdImpression: () -> Unit,
    onFlush: () -> Unit,
    onClear: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Analytics inspector") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = AppTheme.colors.material.surface,
            )
        },
        content = { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedButton(onClick = onSimulateAdImpression) { Text("Ad impression") }
                    OutlinedButton(onClick = onFlush) { Text("Flush") }
                    OutlinedButton(onClick = onClear) { Text("Clear") }
                }
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.entries) { entry ->
                        EntryRow(entry)
                        Divider()
                    }
                }
            }
        },
    )
}

private val timeFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.US)

@Composable
private fun EntryRow(entry: InspectorEntryModel) {
    val color = if (entry.failed) AppTheme.colors.material.error else AppTheme.colors.material.onSurface
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            text = timeFormat.format(Date(entry.at)),
            style = AppTheme.typography.material.caption,
        )
        Text(text = entry.summary, style = AppTheme.typography.material.body2, color = color)
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewInspectorScreen() {
    AppTheme {
        InspectorScreen(
            uiState = InspectorState(entries = fakeInspectorEntryModels),
            onBack = {},
            onSimulateAdImpression = {},
            onFlush = {},
            onClear = {},
        )
    }
}
