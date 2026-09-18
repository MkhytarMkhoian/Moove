package com.moove.movies.analytics.event

import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.parameters

data class MovieOpened(val movieId: Long, val title: String) : Event {
    override val name = "movie_opened"
    override val parameters = parameters {
        put("movie_id", movieId)
        put("title", title)
    }
}
