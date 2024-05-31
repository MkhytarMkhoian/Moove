package com.moove.tickets.presentation.confirmation

import com.moove.shared.navigation.ScreenNavigator

class ConfirmationNavigator(
    private val screenNavigator: ScreenNavigator,
) : ScreenNavigator {

    override fun goBack() {
        screenNavigator.goBack()
    }
}
