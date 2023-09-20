package com.moove.tickets.presentation.fare

import com.moove.core.exception.ExceptionHandler
import com.moove.shared.presentation.compose.component.ScreenContentStatus
import com.moove.tickets.domain.model.Fare
import com.moove.tickets.domain.model.Ryder
import com.moove.tickets.domain.model.randomRyder
import com.moove.tickets.domain.use_cases.GetFaresByIdUseCase
import com.moove.tickets.presentation.fare.model.asPresentation
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test

class FareListViewModelTest {

    companion object {
        private val ryder: Ryder = randomRyder()
        private val ryderId: String = ryder.id
        private val faresList: List<Fare> = ryder.fares
        private val fare: Fare = ryder.fares.first()

        private val defaultState = FareListState(
            status = ScreenContentStatus.Idle,
            fares = emptyList(),
        )
    }

    private val getFaresByIdUseCase: GetFaresByIdUseCase = mockk(relaxed = true)
    private val exceptionHandler = mockk<ExceptionHandler>(relaxed = true)

    private fun TestScope.createViewModel(
        state: FareListState = defaultState,
    ) = FareListViewModel(
        exceptionHandler = exceptionHandler,
        getFaresByIdUseCase = getFaresByIdUseCase,
        ryderId = ryderId,
    ).test(
        initialState = state,
        buildSettings = { isolateFlow = false }
    )

    @Test
    fun `On init fetch fares successfully`() = runTest {
        coEvery { getFaresByIdUseCase(ryderId) } returns faresList
        createViewModel()
            .runOnCreate()
            .assert(defaultState) {
                states(
                    { copy(status = ScreenContentStatus.Loading) },
                    {
                        copy(
                            status = ScreenContentStatus.Success,
                            fares = faresList.asPresentation()
                        )
                    }
                )
            }
        coVerify { getFaresByIdUseCase(ryderId) }
    }

    @Test
    fun `On init fetch fares with error should show error message`() = runTest {
        val error = RuntimeException("test")
        coEvery { getFaresByIdUseCase(ryderId) } throws error
        createViewModel()
            .runOnCreate()
            .assert(defaultState) {
                states(
                    { copy(status = ScreenContentStatus.Loading) },
                    { copy(status = ScreenContentStatus.Failure) }
                )
                postedSideEffects(FareListEffect.ShowGenericError)
            }
        coVerify { getFaresByIdUseCase(ryderId) }
    }

    @Test
    fun `On Fare click post effect`() = runTest {
        createViewModel(defaultState)
            .testIntent { onFareClick(fare.asPresentation()) }
            .assert(defaultState) {
                postedSideEffects(
                    FareListEffect.GoToConfirmation(
                        ryderId = ryder.id,
                        fare = fare.asPresentation()
                    )
                )
            }
    }
}