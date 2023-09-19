package com.moove.shared.presentation.viewmodel

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

val <STATE : Any> ContainerHost<STATE, *>.currentState: STATE
    get() = container.stateFlow.value

/**
 * Collect [ContainerHost.container]'s state via [collectAsState] but bounded with the [flowWithLifecycle]
 */
@Composable
fun <STATE : Any, SIDE_EFFECT : Any> ContainerHost<STATE, SIDE_EFFECT>.composableState(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
): State<STATE> {
    return this.collectAsState(minActiveState)
}

/**
 * Collect [ContainerHost.container]'s effect bounded with the [flowWithLifecycle]
 */
@SuppressLint("ComposableNaming")
@Composable
fun <STATE : Any, SIDE_EFFECT : Any> ContainerHost<STATE, SIDE_EFFECT>.composableEffect(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    action: (suspend (sideEffect: SIDE_EFFECT) -> Unit),
) {
    this.collectSideEffect(sideEffect = action, lifecycleState = minActiveState)
}
