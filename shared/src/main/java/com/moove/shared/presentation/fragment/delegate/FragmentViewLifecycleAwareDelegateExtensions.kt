package com.moove.shared.presentation.fragment.delegate

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentViewLifecycleAwareDelegate(this, viewBindingFactory)
