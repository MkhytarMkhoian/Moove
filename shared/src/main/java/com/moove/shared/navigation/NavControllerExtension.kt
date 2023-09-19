package com.moove.shared.navigation

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.navOptions

fun NavController.navigateSafely(direction: NavDirections, navOptions: NavOptions? = null) {
    currentDestination?.getAction(direction.actionId)?.run {
        val navOptionsWithDefaults = (navOptions ?: this.navOptions ?: navOptions {}).merge(
            navOptions {
                anim {
                    enter = androidx.navigation.ui.R.anim.nav_default_enter_anim
                    exit = androidx.navigation.ui.R.anim.nav_default_exit_anim
                    popEnter = androidx.navigation.ui.R.anim.nav_default_pop_enter_anim
                    popExit = androidx.navigation.ui.R.anim.nav_default_pop_exit_anim
                }
            }
        )
        navigate(direction, navOptionsWithDefaults)
    }
}

/** Override only default (non-defined) parameters with new ones */
private fun NavOptions.merge(new: NavOptions) = navOptions {
    anim {
        enter = if (enterAnim == -1) new.enterAnim else enterAnim
        exit = if (exitAnim == -1) new.exitAnim else exitAnim
        popEnter = if (popEnterAnim == -1) new.popEnterAnim else popEnter
        popExit = if (popExitAnim == -1) new.popExitAnim else popExit
    }
    this.launchSingleTop = shouldLaunchSingleTop() || new.shouldLaunchSingleTop()
    restoreState = shouldRestoreState() || new.shouldRestoreState()
    if (this@merge.popUpToId != -1) popUpTo(this@merge.popUpToId) {
        this.inclusive = isPopUpToInclusive()
        this.saveState = shouldPopUpToSaveState()
    } else popUpTo(new.popUpToId) {
        this.inclusive = new.isPopUpToInclusive()
        this.saveState = new.shouldPopUpToSaveState()
    }
}

fun NavController.goBack(activity: Activity) {
    if (popBackStack()) {
        return
    }
    activity.finish()
}
