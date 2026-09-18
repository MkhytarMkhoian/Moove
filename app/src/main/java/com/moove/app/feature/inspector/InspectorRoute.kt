package com.moove.app.feature.inspector

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.moove.shared.presentation.viewmodel.composableEffect
import com.moove.shared.presentation.viewmodel.composableState
import org.koin.androidx.compose.koinViewModel

@Composable
fun InspectorRoute(
    navigator: InspectorNavigator,
    viewModel: InspectorViewModel = koinViewModel(),
) {
    val state by viewModel.composableState()

    InspectorScreen(
        uiState = state,
        onBack = viewModel::onBack,
        onSimulateAdImpression = viewModel::onSimulateAdImpression,
        onFlush = viewModel::onFlush,
        onClear = viewModel::onClear,
    )

    viewModel.composableEffect { effect ->
        when (effect) {
            InspectorEffect.GoBack -> navigator.goBack()
        }
    }
}
