package com.moove.tickets.presentation.list

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.moove.shared.presentation.compose.component.showGenericError
import com.moove.shared.presentation.viewmodel.composableEffect
import com.moove.shared.presentation.viewmodel.composableState
import org.koin.androidx.compose.getViewModel

@Composable
fun RyderListRoute(
    navigator: RyderListNavigator,
    viewModel: RyderListViewModel = getViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val state by viewModel.composableState()

    RyderListScreen(
        uiState = state,
        scaffoldState = scaffoldState,
        onRyderClick = viewModel::onRyderClick,
    )

    viewModel.RenderEffect(scaffoldState = scaffoldState, navigator = navigator)
}

@Composable
private fun RyderListViewModel.RenderEffect(
    scaffoldState: ScaffoldState,
    navigator: RyderListNavigator,
) {
    val context = LocalContext.current
    composableEffect { effect ->
        when (effect) {
            is RyderListEffect.GoToFares -> {
                navigator.goFares(effect.id)
            }

            RyderListEffect.ShowGenericError -> scaffoldState.showGenericError(context)
        }
    }
}
