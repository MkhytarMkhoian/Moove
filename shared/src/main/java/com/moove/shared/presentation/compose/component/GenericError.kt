package com.moove.shared.presentation.compose.component

import android.content.Context
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import com.moove.shared.R

/**
 * Shows a generic error [Snackbar] at the bottom of the [Scaffold].
 *
 * @param message text to be shown in the [Snackbar]
 * @param duration duration to control how long snackbar will be shown in [SnackbarHost], either
 * [SnackbarDuration.Short], [SnackbarDuration.Long] or [SnackbarDuration.Indefinite]
 * @param onGenericErrorDismissed function executed when the [Snackbar] is dismissed because
 * the duration expires or the user close it
 */
@Composable
fun ScaffoldState.ShowGenericError(
    message: String = genericErrorString,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onGenericErrorDismissed: () -> Unit = {}
) = LaunchedEffect(snackbarHostState) {
    snackbarHostState.showSnackbar(
        message = message,
        duration = duration,
    ).let {
        if (it == SnackbarResult.Dismissed) {
            onGenericErrorDismissed()
        }
    }
}

/**
 * Shows a generic error [Snackbar] at the bottom of the [Scaffold].
 *
 * @param duration duration to control how long snackbar will be shown in [SnackbarHost], either
 * [SnackbarDuration.Short], [SnackbarDuration.Long] or [SnackbarDuration.Indefinite]
 * @param onGenericErrorDismissed function executed when the [Snackbar] is dismissed because
 * the duration expires or the user close it
 */
suspend fun ScaffoldState.showGenericError(
    context: Context,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onGenericErrorDismissed: () -> Unit = {}
) {
    val error = context.getString(R.string.global_snackbar_genericerror_title)
    showSnackBar(error, duration, onGenericErrorDismissed)
}

/**
 * Shows a generic error [Snackbar] at the bottom of the [BottomSheetScaffold].
 *
 * @param duration duration to control how long snackbar will be shown in [SnackbarHost], either
 * [SnackbarDuration.Short], [SnackbarDuration.Long] or [SnackbarDuration.Indefinite]
 * @param onGenericErrorDismissed function executed when the [Snackbar] is dismissed because
 * the duration expires or the user close it
 */
@OptIn(ExperimentalMaterialApi::class)
suspend fun BottomSheetScaffoldState.showGenericError(
    context: Context,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onGenericErrorDismissed: () -> Unit = {}
) {
    val error = context.getString(R.string.global_snackbar_genericerror_title)
    showSnackBar(error, duration, onGenericErrorDismissed)
}

/**
 * Shows a [Snackbar] at the bottom of the [Scaffold].
 *
 * @param message text to be shown in the [Snackbar]
 * @param duration duration to control how long snackbar will be shown in [SnackbarHost], either
 * [SnackbarDuration.Short], [SnackbarDuration.Long] or [SnackbarDuration.Indefinite]
 * @param onDismissed function executed when the [Snackbar] is dismissed because
 * the duration expires or the user close it
 */
suspend fun ScaffoldState.showSnackBar(
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onDismissed: () -> Unit = {}
) = snackbarHostState.showSnackbar(
    message = message,
    duration = duration,
).let {
    if (it == SnackbarResult.Dismissed) {
        onDismissed()
    }
}

/**
 * Shows a [Snackbar] at the bottom of the [BottomSheetScaffold].
 *
 * @param message text to be shown in the [Snackbar]
 * @param duration duration to control how long snackbar will be shown in [SnackbarHost], either
 * [SnackbarDuration.Short], [SnackbarDuration.Long] or [SnackbarDuration.Indefinite]
 * @param onDismissed function executed when the [Snackbar] is dismissed because
 * the duration expires or the user close it
 */
@OptIn(ExperimentalMaterialApi::class)
suspend fun BottomSheetScaffoldState.showSnackBar(
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short,
    onDismissed: () -> Unit = {}
) = snackbarHostState.showSnackbar(
    message = message,
    duration = duration,
).let {
    if (it == SnackbarResult.Dismissed) {
        onDismissed()
    }
}

val genericErrorString: String
    @Composable
    get() = stringResource(id = R.string.global_snackbar_genericerror_title)
