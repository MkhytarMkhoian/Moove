package com.moove.tickets.presentation.fare

import androidx.navigation.NavController
import com.moove.shared.navigation.ScreenNavigator
import com.moove.tickets.presentation.fare.model.FareModel

class FareListNavigator(
    navController: NavController
) : ScreenNavigator(navController) {

    fun goToConfirmation(ryderId: String, fare: FareModel) {

    }
}
