package com.moove.tickets.presentation.list

import com.moove.core.exception.ExceptionHandler
import com.moove.shared.presentation.compose.component.ScreenContentStatus
import com.moove.tickets.domain.model.Ryder
import com.moove.tickets.domain.model.randomRyder
import com.moove.tickets.domain.use_cases.GetRydersUseCase
import com.moove.tickets.presentation.list.model.asPresentation
import io.github.mkhytarmkhoian.herald.testing.FakeAnalyticsProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.orbitmvi.orbit.test.test

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
    private val analytics = FakeAnalyticsProvider()


    private fun createViewModel() = RyderListViewModel(
        exceptionHandler = exceptionHandler,
        getRydersUseCase = getRydersUseCase,
        analyticsEventService = analytics,
    )

    @Test
    fun `On init fetch ryders successfully`() = runTest {
        coEvery { getRydersUseCase() } returns rydersLIst
        createViewModel().test(this, initialState = defaultState) {
            runOnCreate()
            expectState { copy(status = ScreenContentStatus.Loading) }
            expectState {
                copy(
                    status = ScreenContentStatus.Success,
                    ryders = rydersLIst.asPresentation()
                )
            }
        }
        coVerify { getRydersUseCase() }
    }

    @Test
    fun `On init fetch ryders with error should show error message`() = runTest {
        val error = RuntimeException("test")
        coEvery { getRydersUseCase() } throws error
        createViewModel().test(this, initialState = defaultState) {
            runOnCreate()
            expectState { copy(status = ScreenContentStatus.Loading) }
            expectState { copy(status = ScreenContentStatus.Failure) }
            expectSideEffect(RyderListEffect.ShowGenericError)
        }
        coVerify { getRydersUseCase() }
    }

    @Test
    fun `On Ryder click post effect`() = runTest {
        createViewModel().test(this, initialState = defaultState) {
            containerHost.onRyderClick(ryder.asPresentation())
            expectSideEffect(RyderListEffect.GoToFares(ryder.id))
        }

        analytics.assertTracked("ryder_selected") { param("ryder_id", ryder.id) }
        analytics.assertNothingElseTracked()
    }

    @Test
    fun `On init the screen view is tracked before anything else`() = runTest {
        coEvery { getRydersUseCase() } returns rydersLIst
        createViewModel().test(this, initialState = defaultState) {
            runOnCreate()
            expectState { copy(status = ScreenContentStatus.Loading) }
            expectState { copy(status = ScreenContentStatus.Success, ryders = rydersLIst.asPresentation()) }
        }

        analytics.assertTracked("ryder_list_shown")
        analytics.assertNothingElseTracked()
    }
}