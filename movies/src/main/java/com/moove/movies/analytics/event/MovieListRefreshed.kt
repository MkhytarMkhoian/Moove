package com.moove.movies.analytics.event

import io.github.mkhytarmkhoian.herald.Event

/** The user pulled to refresh. */
data object MovieListRefreshed : Event {
    override val name = "movie_list_refreshed"
}
