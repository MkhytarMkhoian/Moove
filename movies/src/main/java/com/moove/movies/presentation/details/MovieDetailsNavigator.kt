package com.moove.movies.presentation.details

import com.moove.shared.navigation.ScreenNavigator

class MovieDetailsNavigator(
    private val screenNavigator: ScreenNavigator,
) : ScreenNavigator {

    override fun goBack() {
        screenNavigator.goBack()
    }
}
