package com.moove.inspector.presentation

import com.moove.shared.navigation.ScreenNavigator

class InspectorNavigator(
    private val screenNavigator: ScreenNavigator,
) : ScreenNavigator {

    override fun goBack() {
        screenNavigator.goBack()
    }
}
