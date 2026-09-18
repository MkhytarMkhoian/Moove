package com.moove.movies.analytics.event

import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.parameters

data class MovieDetailsRetried(val movieId: Long) : Event {
    override val name = "movie_details_retried"
    override val parameters = parameters { put("movie_id", movieId) }
}
