package com.moove.shared.presentation.compose.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

fun Fragment.setAppComposeContent(view: ComposeView, content: @Composable () -> Unit) {
    view.setAppComposeContent(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed, content)
}

@Composable
fun rememberFragmentManager(): FragmentManager? {
    val context = LocalContext.current
    return remember(context) {
        (context as? FragmentActivity)?.supportFragmentManager
    }
}
