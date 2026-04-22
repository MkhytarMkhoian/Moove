package com.moove.app.feature.deeplink.presentation

import com.moove.app.feature.deeplink.domain.AppDeepLink
import com.moove.shared.feature.deeplink.domain.DeepLink
import com.moove.shared.feature.deeplink.presentation.DeepLinkNavigator
import com.moove.shared.navigation.GlobalAppNavigator
import com.moove.shared.navigation.MoviesNavigator
import com.moove.shared.navigation.TicketsNavigator

class DeepLinkAppNavigator(
    private val globalAppNavigator: GlobalAppNavigator,
    private val ticketsNavigator: TicketsNavigator,
    private val moviesNavigator: MoviesNavigator,
) : DeepLinkNavigator {
    override fun navigateTo(link: DeepLink) {
        when (link) {
            is AppDeepLink.FareList -> ticketsNavigator.goFares(link.ryderId)
            is AppDeepLink.Confirmation -> {
                ticketsNavigator.goFares(link.ryderId)
                ticketsNavigator.goToConfirmation(
                    ryderId = link.ryderId,
                    fareDescription = link.fare.description,
                    farePrice = link.fare.price
                )
            }

            is AppDeepLink.MovieDetails -> moviesNavigator.goToMovieDetails(link.movieId)

            is AppDeepLink.Home, AppDeepLink.Unknown -> globalAppNavigator.goHome()
        }
    }
}
