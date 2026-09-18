package com.moove.movies.analytics.event

import io.github.mkhytarmkhoian.herald.ScreenViewEvent

/** The list screen becoming visible. Tracked from the ViewModel, once per ViewModel. */
data object MovieListScreenViewed : ScreenViewEvent {
    override val name = "movie_list_shown"
    override val screenName = "MovieList"
}
