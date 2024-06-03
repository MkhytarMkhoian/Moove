package com.moove.app.main

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.moove.core.exception.ExceptionHandler
import com.moove.core.exception.asCoroutineExceptionHandler
import com.moove.shared.feature.deeplink.domain.GetDeeplinkUseCase
import com.moove.shared.presentation.viewmodel.executeUseCase
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container

class MainActivityViewModel(
    exceptionHandler: ExceptionHandler,
    private val getDeeplinkUseCase: GetDeeplinkUseCase,
) : ViewModel(), ContainerHost<MainActivityState, MainActivityEffect> {

    override val container: Container<MainActivityState, MainActivityEffect> = container(
        initialState = MainActivityState(),
        buildSettings = {
            this.exceptionHandler = exceptionHandler.asCoroutineExceptionHandler()
        },
    )

    fun handleIntent(intent: Intent?) = intent {
        if (intent == null) return@intent

        val uri = getDeepLinkFromIntent(intent)
        if (uri.isNullOrEmpty()) return@intent

        executeUseCase { getDeeplinkUseCase(uri) }
            .onSuccess { deeplink -> postSideEffect(MainActivityEffect.NavigateDeepLink(deeplink)) }
            .onFailure { postSideEffect(MainActivityEffect.ShowGenericError) }
    }

    private fun getDeepLinkFromIntent(intent: Intent): String? {
        return intent.takeIf {
            Intent.ACTION_VIEW == it.action || Intent.ACTION_MAIN == it.action
        }?.dataString
    }
}
