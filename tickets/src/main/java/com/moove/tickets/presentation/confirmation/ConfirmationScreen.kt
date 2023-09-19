package com.moove.tickets.presentation.confirmation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.moove.design_system.compose.AppButtonDefaults
import com.moove.design_system.compose.AppTheme
import com.moove.design_system.compose.PrimaryButton
import com.moove.design_system.compose.Scaffold
import com.moove.design_system.compose.Spacing
import com.moove.shared.R
import com.moove.shared.presentation.compose.component.ScreenContentStatus
import com.moove.tickets.presentation.confirmation.component.ConfirmationItem
import com.moove.tickets.presentation.fare.model.fakeFareModels

@Composable
fun ConfirmationScreen(
    uiState: ConfirmationState,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onIncrementTicketClick: () -> Unit,
    onDecrementTicketClick: () -> Unit,
    onConfirmClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.confirm_selection_title))
                },
                backgroundColor = AppTheme.colors.material.surface,
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = Spacing.S)
            ) {
                ConfirmationItem(
                    modifier = Modifier.align(Alignment.TopStart),
                    ryderId = uiState.ryderId,
                    fare = uiState.fare,
                    ticketCount = uiState.ticketCount,
                    onIncrementTicketClick = onIncrementTicketClick,
                    onDecrementTicketClick = onDecrementTicketClick
                )

                PrimaryButton(
                    modifier = AppButtonDefaults.Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = Spacing.S),
                    onClick = onConfirmClick,
                    content = {
                        Text(
                            text = stringResource(
                                id = R.string.confirm_button_text,
                                uiState.ticketCount,
                                "$${uiState.totalPrice}"
                            )
                        )
                    },
                )
            }
        }
    )
}

@Preview(name = "Confirmation Content", showBackground = true)
@Composable
fun PreviewConfirmationContent() {
    AppTheme {
        ConfirmationScreen(
            uiState = ConfirmationState(
                status = ScreenContentStatus.Success,
                ryderId = "Adult",
                fare = fakeFareModels.first(),
                ticketCount = 2,
                totalPrice = fakeFareModels.first().price * 2
            ),
            onIncrementTicketClick = {},
            onDecrementTicketClick = {},
            onConfirmClick = {},
        )
    }
}