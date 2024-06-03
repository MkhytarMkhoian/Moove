package com.moove.tickets.presentation.list

import com.moove.shared.navigation.ScreenNavigator
import com.moove.shared.navigation.TicketsNavigator

class RyderListNavigator(
    private val ticketsNavigator: TicketsNavigator,
    private val screenNavigator: ScreenNavigator,
) : ScreenNavigator {

    fun goFares(id: String) {
        ticketsNavigator.goFares(id)
    }

    override fun goBack() {
        screenNavigator.goBack()
    }
}
