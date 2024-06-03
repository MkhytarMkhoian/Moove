package com.moove.app.feature.home

import androidx.lifecycle.ViewModel
import com.moove.core.exception.ExceptionHandler
import com.moove.core.exception.asCoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container

class HomeViewModel(
    exceptionHandler: ExceptionHandler,
) : ViewModel(), ContainerHost<HomeState, HomeEffect> {

    override val container: Container<HomeState, HomeEffect> = container(
        initialState = HomeState(),
        buildSettings = {
            this.exceptionHandler = exceptionHandler.asCoroutineExceptionHandler()
        },
    )

    fun onRyderClick() = intent {
        postSideEffect(HomeEffect.GoToRyderList)
    }
}
