package com.moove.movies.presentation.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.moove.shared.R
import com.moove.shared.databinding.ComposeFragmentBinding
import com.moove.shared.presentation.compose.platform.setAppComposeContent
import com.moove.shared.presentation.fragment.delegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.compose_fragment) {

    private val viewBinding by viewBinding(ComposeFragmentBinding::bind)
    private val navigator: MovieDetailsNavigator by inject {
        parametersOf(findNavController(), lifecycleScope, requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAppComposeContent(viewBinding.compose) {
            MovieDetailsRoute(navigator = navigator)
        }
    }
}
