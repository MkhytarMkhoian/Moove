package com.moove.tickets.presentation.fare

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.moove.shared.presentation.compose.component.showGenericError
import com.moove.shared.presentation.viewmodel.composableEffect
import com.moove.shared.presentation.viewmodel.composableState
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun FareListRoute(
    navigator: FareListNavigator,
    ryderId: String,
    viewModel: FareListViewModel = getViewModel { parametersOf(ryderId) },
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val state by viewModel.composableState()

    FareListScreen(
        uiState = state,
        scaffoldState = scaffoldState,
        onFareClick = viewModel::onFareClick,
    )

    viewModel.RenderEffect(scaffoldState = scaffoldState, navigator = navigator)
}

@Composable
private fun FareListViewModel.RenderEffect(
    scaffoldState: ScaffoldState,
    navigator: FareListNavigator,
) {
    val context = LocalContext.current
    composableEffect { effect ->
        when (effect) {
            is FareListEffect.GoToConfirmation -> {
                navigator.goToConfirmation(ryderId = effect.ryderId, fare = effect.fare)
            }

            FareListEffect.ShowGenericError -> scaffoldState.showGenericError(context)
        }
    }
}
