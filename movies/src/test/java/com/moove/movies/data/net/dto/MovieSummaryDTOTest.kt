package com.moove.movies.data.net.dto

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MovieSummaryDTOTest {

    @Test
    fun `given a dto when mapping to domain then copies primitive fields`() {
        val dto = randomMovieSummaryDTO(
            id = 42L,
            title = "The Matrix",
            posterPath = "/poster.jpg",
            voteAverage = 8.2,
            releaseDate = "1999-03-31",
        )

        val domain = dto.asDomain()

        assertEquals(42L, domain.id)
        assertEquals("The Matrix", domain.title)
        assertEquals("/poster.jpg", domain.posterPath)
        assertEquals(8.2f, domain.rating)
        assertEquals(LocalDate.of(1999, 3, 31), domain.releaseDate)
    }

    @Test
    fun `given a blank release date when mapping then produces null`() {
        val dto = randomMovieSummaryDTO(releaseDate = "")

        val domain = dto.asDomain()

        assertNull(domain.releaseDate)
    }

    @Test
    fun `given a malformed release date when mapping then produces null`() {
        val dto = randomMovieSummaryDTO(releaseDate = "not-a-date")

        val domain = dto.asDomain()

        assertNull(domain.releaseDate)
    }

    @Test
    fun `given a null poster path when mapping then produces null`() {
        val dto = randomMovieSummaryDTO(posterPath = null)

        val domain = dto.asDomain()

        assertNull(domain.posterPath)
    }

    @Test
    fun `given a list of dtos when mapping then each element is converted`() {
        val dtos = listOf(randomMovieSummaryDTO(id = 1), randomMovieSummaryDTO(id = 2))

        val domain = dtos.asDomain()

        assertEquals(listOf(1L, 2L), domain.map { it.id })
    }
}
