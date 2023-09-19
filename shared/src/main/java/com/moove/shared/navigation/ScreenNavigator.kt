package com.moove.shared.navigation

import androidx.navigation.NavController

abstract class ScreenNavigator(
    private val navController: NavController
) {
    open fun goBack() {
        navController.navigateUp()
    }
}
