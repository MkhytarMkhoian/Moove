package com.moove.app.main

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.moove.analytics.domain.use_cases.StartAnalyticsUseCase
import com.moove.app.analytics.event.DeepLinkOpened
import com.moove.core.exception.ExceptionHandler
import com.moove.core.exception.asCoroutineExceptionHandler
import com.moove.shared.feature.deeplink.domain.GetDeeplinkUseCase
import com.moove.shared.presentation.viewmodel.executeUseCase
import io.github.mkhytarmkhoian.herald.EventTrackerService
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class MainActivityViewModel(
    private val exceptionHandler: ExceptionHandler,
    private val getDeeplinkUseCase: GetDeeplinkUseCase,
    private val startAnalyticsUseCase: StartAnalyticsUseCase,
    private val analyticsEventService: EventTrackerService,
) : ViewModel(), ContainerHost<MainActivityState, MainActivityEffect> {

    override val container: Container<MainActivityState, MainActivityEffect> = container(
        initialState = MainActivityState(),
        buildSettings = {
            this.exceptionHandler =
                this@MainActivityViewModel.exceptionHandler.asCoroutineExceptionHandler()
        },
    ) {
        // The single entry point, deep links included, so nothing tracks before the vendors start.
        executeUseCase(exceptionHandler) { startAnalyticsUseCase() }
    }

    fun handleIntent(intent: Intent?) = intent {
        if (intent == null) return@intent

        val uri = getDeepLinkFromIntent(intent)
        if (uri.isNullOrEmpty()) return@intent

        executeUseCase(exceptionHandler) { getDeeplinkUseCase(uri) }
            .onSuccess { deeplink ->
                val parsed = Uri.parse(uri)
                analyticsEventService.track(
                    DeepLinkOpened(
                        host = parsed.host.orEmpty(),
                        path = parsed.path.orEmpty()
                    )
                )
                postSideEffect(MainActivityEffect.NavigateDeepLink(deeplink))
            }
            .onFailure { postSideEffect(MainActivityEffect.ShowGenericError) }
    }

    private fun getDeepLinkFromIntent(intent: Intent): String? {
        return intent.takeIf {
            Intent.ACTION_VIEW == it.action || Intent.ACTION_MAIN == it.action
        }?.dataString
    }
}
