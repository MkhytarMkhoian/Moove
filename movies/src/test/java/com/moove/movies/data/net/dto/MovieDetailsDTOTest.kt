package com.moove.movies.data.net.dto

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MovieDetailsDTOTest {

    @Test
    fun `given a dto with null overview when mapping then overview is empty`() {
        val dto = randomMovieDetailsDTO(overview = null)

        val domain = dto.asDomain()

        assertEquals("", domain.overview)
    }

    @Test
    fun `given a dto with zero runtime when mapping then runtime is null`() {
        val dto = randomMovieDetailsDTO(runtimeMinutes = 0)

        val domain = dto.asDomain()

        assertNull(domain.runtimeMinutes)
    }

    @Test
    fun `given a dto with null genres when mapping then genres are empty`() {
        val dto = randomMovieDetailsDTO(genres = null)

        val domain = dto.asDomain()

        assertTrue(domain.genres.isEmpty())
    }

    @Test
    fun `given a dto with genres when mapping then each genre is converted`() {
        val dto = randomMovieDetailsDTO(
            genres = listOf(randomGenreDTO(id = 1, name = "Action")),
        )

        val domain = dto.asDomain()

        assertEquals(1, domain.genres.size)
        assertEquals(1L, domain.genres.first().id)
        assertEquals("Action", domain.genres.first().name)
    }
}
