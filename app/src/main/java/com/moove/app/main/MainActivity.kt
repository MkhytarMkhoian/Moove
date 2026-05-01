package com.moove.app.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.moove.R
import com.moove.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainActivityViewModel by viewModel()
    private val navigator: MainNavigator by inject {
        parametersOf(
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
                .navController,
            lifecycleScope,
            this
        )
    }
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        mainViewModel.handleIntent(intent)

        observeSideEffect()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        mainViewModel.handleIntent(intent)
    }

    private fun observeSideEffect() {
        lifecycleScope.launch {
            val sideEffectFlow = mainViewModel.container.sideEffectFlow
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sideEffectFlow.collect { effect ->
                    when (effect) {
                        is MainActivityEffect.NavigateDeepLink -> navigator.navigateDeepLink(effect.deepLink)
                        MainActivityEffect.ShowGenericError -> Toast.makeText(
                            this@MainActivity,
                            "Generic Error",
                            Toast.LENGTH_SHORT
                        )
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        val backStackEntryCount = supportFragmentManager.primaryNavigationFragment
            ?.childFragmentManager
            ?.backStackEntryCount ?: 0

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q &&
            isTaskRoot &&
            backStackEntryCount == 0 && supportFragmentManager.backStackEntryCount == 0
        ) {
            finishAfterTransition()
        } else {
            super.onBackPressed()
        }
    }
}
