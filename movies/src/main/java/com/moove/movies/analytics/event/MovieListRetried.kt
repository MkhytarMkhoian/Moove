package com.moove.movies.analytics.event

import io.github.mkhytarmkhoian.herald.Event
import io.github.mkhytarmkhoian.herald.parameters

/** The user tapped retry after a load failed; [stage] says which load. */
data class MovieListRetried(val stage: Stage) : Event {

    enum class Stage(val wireName: String) {
        INITIAL_LOAD("initial_load"),
        NEXT_PAGE("next_page"),
    }

    override val name = "movie_list_retried"
    override val parameters = parameters { put("stage", stage.wireName) }
}
