package com.moove.tickets.presentation.fare

import androidx.navigation.NavController
import com.moove.shared.navigation.ScreenNavigator
import com.moove.shared.navigation.navigateSafely
import com.moove.tickets.presentation.fare.model.FareModel

class FareListNavigator(
    private val navController: NavController
) : ScreenNavigator(navController) {

    fun goToConfirmation(ryderId: String, fare: FareModel) {
        navController.navigateSafely(
            FareListFragmentDirections.actionFareListFragmentToConfirmationFragment(
                ryderId = ryderId,
                fare = fare
            )
        )
    }
}
