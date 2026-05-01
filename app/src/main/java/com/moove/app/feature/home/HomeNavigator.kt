package com.moove.app.feature.home

import androidx.navigation.NavController
import com.moove.shared.navigation.ScreenNavigator
import com.moove.shared.navigation.navigateSafely

class HomeNavigator(
    private val navController: NavController,
    private val screenNavigator: ScreenNavigator,
) : ScreenNavigator {

    fun goRyderList() {
        navController.navigateSafely(HomeFragmentDirections.actionHomeFragmentToTicketsFlow())
    }

    fun goMovies() {
        navController.navigateSafely(HomeFragmentDirections.actionHomeFragmentToMoviesFlow())
    }

    override fun goBack() {
        screenNavigator.goBack()
    }
}
