package com.moove.app.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.moove.core.exception.ExceptionHandler
import com.moove.core.exception.asCoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class MainActivityViewModel(
    exceptionHandler: ExceptionHandler,
    savedStateHandle: SavedStateHandle,
) : ViewModel(),
    ContainerHost<MainActivityState, MainActivityEffect> {

    override val container: Container<MainActivityState, MainActivityEffect> = container(
        initialState = MainActivityState(),
        savedStateHandle = savedStateHandle,
        buildSettings = {
            this.exceptionHandler = exceptionHandler.asCoroutineExceptionHandler()
        },
    )
}
