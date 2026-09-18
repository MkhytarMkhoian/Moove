package com.moove.movies.analytics.event

import io.github.mkhytarmkhoian.herald.ScreenViewEvent
import io.github.mkhytarmkhoian.herald.parameters

/** The details screen becoming visible. Tracked from the composable, each time it resumes. */
data class MovieDetailsScreenViewed(val movieId: Long) : ScreenViewEvent {
    override val name = "movie_details_shown"
    override val screenName = "MovieDetails"
    override val parameters = parameters { put("movie_id", movieId) }
}
