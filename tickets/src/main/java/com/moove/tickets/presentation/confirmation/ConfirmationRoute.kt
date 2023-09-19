package com.moove.tickets.presentation.confirmation

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.moove.shared.R
import com.moove.shared.presentation.compose.component.showGenericError
import com.moove.shared.presentation.compose.component.showSnackBar
import com.moove.shared.presentation.viewmodel.composableEffect
import com.moove.shared.presentation.viewmodel.composableState
import com.moove.tickets.presentation.fare.model.FareModel
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun FareListRoute(
    navigator: ConfirmationNavigator,
    ryderId: String,
    fare: FareModel,
    viewModel: ConfirmationViewModel = getViewModel { parametersOf(ryderId, fare) },
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val state by viewModel.composableState()

    ConfirmationScreen(
        uiState = state,
        scaffoldState = scaffoldState,
        onIncrementTicketClick = viewModel::onIncrementTicketClick,
        onDecrementTicketClick = viewModel::onDecrementTicketClick,
        onConfirmClick = viewModel::onConfirmClick,
    )

    viewModel.RenderEffect(scaffoldState = scaffoldState, navigator = navigator)
}

@Composable
private fun ConfirmationViewModel.RenderEffect(
    scaffoldState: ScaffoldState,
    navigator: ConfirmationNavigator,
) {
    val context = LocalContext.current
    composableEffect { effect ->
        when (effect) {
            ConfirmationEffect.ShowGenericError -> scaffoldState.showGenericError(context)
            ConfirmationEffect.ShowSuccessMessage -> scaffoldState.showSnackBar(
                context.getString(R.string.confirm_success)
            )
        }
    }
}
