package com.moove.tickets.presentation.list

import com.moove.core.exception.ExceptionHandler
import com.moove.shared.presentation.compose.component.ScreenContentStatus
import com.moove.tickets.domain.model.Ryder
import com.moove.tickets.domain.model.randomRyder
import com.moove.tickets.domain.use_cases.GetRydersUseCase
import com.moove.tickets.presentation.list.model.asPresentation
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test

class RyderListViewModelTest {

    companion object {
        private val ryder: Ryder = randomRyder()
        private val rydersLIst = listOf(
            randomRyder(),
            ryder,
            randomRyder(),
        )
        private val defaultState = RyderListState(
            status = ScreenContentStatus.Idle,
            ryders = emptyList(),
        )
    }

    private val getRydersUseCase: GetRydersUseCase = mockk(relaxed = true)
    private val exceptionHandler = mockk<ExceptionHandler>(relaxed = true)

    private fun TestScope.createViewModel(
        state: RyderListState = defaultState,
    ) = RyderListViewModel(
        exceptionHandler = exceptionHandler,
        getRydersUseCase = getRydersUseCase,
    ).test(
        initialState = state,
        buildSettings = { isolateFlow = false }
    )

    @Test
    fun `On init fetch ryders successfully`() = runTest {
        coEvery { getRydersUseCase() } returns rydersLIst
        createViewModel()
            .runOnCreate()
            .assert(defaultState) {
                states(
                    { copy(status = ScreenContentStatus.Loading) },
                    {
                        copy(
                            status = ScreenContentStatus.Success,
                            ryders = rydersLIst.asPresentation()
                        )
                    }
                )
            }
        coVerify { getRydersUseCase() }
    }

    @Test
    fun `On init fetch ryders with error should show error message`() = runTest {
        val error = RuntimeException("test")
        coEvery { getRydersUseCase() } throws error
        createViewModel()
            .runOnCreate()
            .assert(defaultState) {
                states(
                    { copy(status = ScreenContentStatus.Loading) },
                    { copy(status = ScreenContentStatus.Failure) }
                )
                postedSideEffects(RyderListEffect.ShowGenericError)
            }
        coVerify { getRydersUseCase() }
    }

    @Test
    fun `On Ryder click post effect`() = runTest {
        createViewModel(defaultState)
            .testIntent { onRyderClick(ryder.asPresentation()) }
            .assert(defaultState) {
                postedSideEffects(RyderListEffect.GoToFares(ryder.id))
            }
    }
}