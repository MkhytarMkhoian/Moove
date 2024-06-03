package com.moove.shared.navigation

interface TicketsNavigator {

    fun goFares(ryderId: String)
    fun goToConfirmation(ryderId: String, fareDescription: String, farePrice: Float)
}