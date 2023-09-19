package com.moove.shared.presentation.compose.component

import android.os.Parcelable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.parcelize.Parcelize

@Composable
fun ScreenContent(
    modifier: Modifier = Modifier,
    status: ScreenContentStatus,
    forceLoading: Boolean = false,
    onRetry: () -> Unit = {},
    error: @Composable () -> Unit = { },
    blockingSurface: @Composable BoxScope.() -> Unit = defaultBlockingSurface,
    content: @Composable BoxScope.() -> Unit,
) {
    val delay = if (forceLoading) BlockingBoxDefaults.zeroDelay else BlockingBoxDefaults.shortDelay
    BlockingBox(
        modifier = modifier,
        blocked = status.isLoading || forceLoading,
        delayBeforeBlock = delay,
        minTimeToShow = delay,
        blockingSurface = blockingSurface,
    ) {
        if (status.isFailing) {
            error()
        } else if (status.isSuccess || status.isRefreshing) {
            content()
        }
    }
}

sealed class ScreenContentStatus : Parcelable {

    companion object {
        fun asProgress(isRefreshing: Boolean): ScreenContentStatus =
            if (isRefreshing) Refreshing else Loading
    }

    @Parcelize
    object Idle : ScreenContentStatus()

    @Parcelize
    object Loading : ScreenContentStatus()

    @Parcelize
    object Refreshing : ScreenContentStatus()

    @Parcelize
    object Success : ScreenContentStatus()

    @Parcelize
    object Failure : ScreenContentStatus()
}

val ScreenContentStatus.inProgress: Boolean
    get() = this.isLoading || this.isRefreshing

val ScreenContentStatus.isLoading: Boolean
    get() = this is ScreenContentStatus.Loading

val ScreenContentStatus.isRefreshing: Boolean
    get() = this is ScreenContentStatus.Refreshing

val ScreenContentStatus.isFailing: Boolean
    get() = this is ScreenContentStatus.Failure

val ScreenContentStatus.isSuccess: Boolean
    get() = this == ScreenContentStatus.Success
