package com.moove.tickets.presentation.fare

import com.moove.shared.navigation.ScreenNavigator
import com.moove.shared.navigation.TicketsNavigator
import com.moove.tickets.presentation.fare.model.FareModel

class FareListNavigator(
    private val ticketsNavigator: TicketsNavigator,
    private val screenNavigator: ScreenNavigator,
) : ScreenNavigator {

    fun goToConfirmation(ryderId: String, fare: FareModel) {
        ticketsNavigator.goToConfirmation(ryderId, fare.description, fare.price)
    }

    override fun goBack() {
        screenNavigator.goBack()
    }
}
