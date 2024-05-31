package com.moove.app.navigation

import androidx.navigation.NavController
import com.moove.MobileNavigationDirections
import com.moove.app.feature.home.HomeFragmentDirections
import com.moove.shared.navigation.GlobalAppNavigator
import com.moove.shared.navigation.navigateSafely
import com.moove.tickets.presentation.fare.FareListFragmentDirections
import com.moove.tickets.presentation.fare.model.FareModel
import com.moove.tickets.presentation.list.RyderListFragmentDirections

class AppNavigator(
    private val navController: NavController,
) : GlobalAppNavigator {

    override fun goFares(ryderId: String) {
        navController.navigateSafely(HomeFragmentDirections.actionHomeFragmentToTicketsFlow())
        navController.navigateSafely(
            RyderListFragmentDirections.actionRydersFragmentToFareListFragment(ryderId = ryderId)
        )
    }

    override fun goToConfirmation(ryderId: String, fareDescription: String, farePrice: Float) {
        navController.navigateSafely(
            FareListFragmentDirections.actionFareListFragmentToConfirmationFragment(
                ryderId = ryderId,
                fare = FareModel(
                    description = fareDescription,
                    price = farePrice
                )
            )
        )
    }

    override fun goBack() {
        navController.navigateUp()
    }

    override fun goHome() {
        navController.navigateSafely(
            MobileNavigationDirections.actionGlobalGoHome()
        )
    }
}