package com.moove.shared.presentation.fragment.delegate

import android.content.BroadcastReceiver
import android.content.IntentFilter
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

fun <T : Any> Fragment.registerLifecycleAwareItem(
    state: Lifecycle.State = Lifecycle.State.CREATED,
    itemFactory: () -> T,
    tearDown: ((T) -> Unit)? = null,
) {
    var item: T? = null

    fun applyFactory() {
        item = itemFactory()
    }

    fun applyTearDown() {
        item = item?.let { tearDown?.invoke(it); null }
    }
    lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
            if (state == Lifecycle.State.CREATED) applyFactory()
        }

        override fun onStart(owner: LifecycleOwner) {
            if (state == Lifecycle.State.STARTED) applyFactory()
        }

        override fun onResume(owner: LifecycleOwner) {
            if (state == Lifecycle.State.RESUMED) applyFactory()
        }

        override fun onPause(owner: LifecycleOwner) {
            if (state == Lifecycle.State.RESUMED) applyTearDown()
        }

        override fun onStop(owner: LifecycleOwner) {
            if (state == Lifecycle.State.STARTED) applyTearDown()
        }

        override fun onDestroy(owner: LifecycleOwner) {
            if (state == Lifecycle.State.CREATED) applyTearDown()
        }
    })
}

/**
 * Create Delegate to apply/revert activity-wide [WindowCompat.setDecorFitsSystemWindows]
 * on fragment start-stop lifecycle.
 * @param revertOnDestroy if false, no tearDown is applied:
 * useful if the next navigation destination also applies this method (due to [Fragment] lifecycle).
 */
fun Fragment.setDecorFitsSystemWindows(fits: Boolean, revertOnDestroy: Boolean = true) =
    registerLifecycleAwareItem(
        state = Lifecycle.State.STARTED,
        itemFactory = { WindowCompat.setDecorFitsSystemWindows(requireActivity().window, fits) },
        tearDown = { if (revertOnDestroy) WindowCompat.setDecorFitsSystemWindows(requireActivity().window, !fits) },
    )

fun <T : BroadcastReceiver> Fragment.registerBroadcastReceiver(receiver: T, filter: IntentFilter = IntentFilter()) =
    registerLifecycleAwareItem(
        state = Lifecycle.State.CREATED,
        itemFactory = {
            requireContext().registerReceiver(receiver, filter)
            Unit
        },
        tearDown = { requireContext().unregisterReceiver(receiver) }
    )
