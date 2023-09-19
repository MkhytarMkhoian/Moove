package com.moove.tickets.presentation.list

import androidx.navigation.NavController
import com.moove.shared.navigation.ScreenNavigator
import com.moove.shared.navigation.navigateSafely

class RyderListNavigator(
    private val navController: NavController
) : ScreenNavigator(navController) {

    fun goFares(id: String) {
        navController.navigateSafely(
            RyderListFragmentDirections.actionRydersFragmentToFareListFragment(ryderId = id)
        )
    }
}
