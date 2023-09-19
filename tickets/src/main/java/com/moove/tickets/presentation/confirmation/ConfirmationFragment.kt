package com.moove.tickets.presentation.confirmation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.moove.shared.R
import com.moove.shared.databinding.ComposeFragmentBinding
import com.moove.shared.presentation.compose.platform.setAppComposeContent
import com.moove.shared.presentation.fragment.delegate.viewBinding
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ConfirmationFragment : Fragment(R.layout.compose_fragment) {

    private val arguments: ConfirmationFragmentArgs by navArgs()
    private val viewBinding by viewBinding(ComposeFragmentBinding::bind)
    private val navigator: ConfirmationNavigator by inject {
        parametersOf(findNavController(), lifecycleScope, requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAppComposeContent(viewBinding.compose) {
            FareListRoute(
                navigator = navigator,
                ryderId = arguments.ryderId,
                fare = arguments.fare
            )
        }
    }
}
