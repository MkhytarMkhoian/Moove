package com.moove.movies.analytics.event

import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.parameters

/**
 * A movie card was actually seen: at least half of it inside the window. Tracked by
 * `Modifier.trackImpression` on the grid item, once per appearance — scrolling it away and back
 * counts again.
 */
data class MovieImpression(val movieId: Long) : Event {
    override val name = "movie_impression"
    override val parameters = parameters { put("movie_id", movieId) }
}
