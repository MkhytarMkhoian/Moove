package com.moove.shared.presentation.fragment.delegate

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentViewLifecycleAwareDelegate<T : Any>(
    val fragment: Fragment,
    val itemFactory: (View) -> T,
    val tearDown: ((T) -> Unit)? = null,
) : ReadOnlyProperty<Fragment, T> {

    private var item: T? = null
    private val viewLifecycleOwnerLiveDataObserver = Observer<LifecycleOwner?> {
        val viewLifecycleOwner = it ?: return@Observer

        viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                if (tearDown != null) item?.also(tearDown)
                item = null
            }
        })
    }

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {

            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observeForever(
                    viewLifecycleOwnerLiveDataObserver
                )
            }

            override fun onDestroy(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.removeObserver(
                    viewLifecycleOwnerLiveDataObserver
                )
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val binding = item
        if (binding != null) {
            return binding
        }
        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            throw IllegalStateException("Should not attempt to get bindings when Fragment views are destroyed.")
        }

        return itemFactory(thisRef.requireView()).also { this.item = it }
    }
}
