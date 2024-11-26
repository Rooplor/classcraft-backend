package com.rooplor.classcraftbackend.repositories

import com.rooplor.classcraftbackend.entities.Venue
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito.`when`
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.Optional
import kotlin.test.Test

@DataMongoTest
class VenueRepositoryTest {
    @MockBean
    private lateinit var venueRepository: VenueRepository

    @Test
    fun `should save and find venue by id`() {
        val venueToSave =
            Venue(
                id = "1",
                room = "TRAIN_3",
            )
        `when`(venueRepository.save(venueToSave)).thenReturn(venueToSave)
        `when`(venueRepository.findById(venueToSave.id!!)).thenReturn(Optional.of(venueToSave))

        val savedVenue = venueRepository.save(venueToSave)
        val foundVenue = venueRepository.findById(savedVenue.id!!).get()
        assertEquals(savedVenue, foundVenue)
    }

    @Test
    fun `should find all venues`() {
        val venues =
            listOf(
                Venue(
                    id = "1",
                    room = "TRAIN_3",
                ),
                Venue(
                    id = "2",
                    room = "TRAIN_4",
                ),
            )
        `when`(venueRepository.findAll()).thenReturn(venues)

        val foundVenues = venueRepository.findAll()
        assertEquals(venues, foundVenues)
    }
}
