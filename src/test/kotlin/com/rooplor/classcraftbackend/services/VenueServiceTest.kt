package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.Venue
import com.rooplor.classcraftbackend.repositories.VenueRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import java.util.Optional
import kotlin.test.Test

@SpringBootTest
class VenueServiceTest {
    private val venueRepository: VenueRepository = Mockito.mock(VenueRepository::class.java)

    private val venueService: VenueService = VenueService(venueRepository)

    @Test
    fun `should return all venues`() {
        val venues = listOf(Venue("1", "TRAIN_3"), Venue("2", "TRAIN_4"))
        Mockito.`when`(venueRepository.findAll()).thenReturn(venues)

        val result = venueService.findAllVenue()
        assertEquals(venues, result)
    }

    @Test
    fun `should return venue by id`() {
        val venueId = "1"
        val venue = Venue("1", "TRAIN_3")
        Mockito.`when`(venueRepository.findById(venueId)).thenReturn(Optional.of(venue))

        val result = venueService.findVenueById(venueId)
        assertEquals(venue, result)
    }

    @Test
    fun `should insert a venue`() {
        val venue = Venue("1", "TRAIN_3")
        Mockito.`when`(venueRepository.insert(venue)).thenReturn(venue)

        val result = venueService.insertVenue(venue)
        assertEquals(venue, result)
    }
}
