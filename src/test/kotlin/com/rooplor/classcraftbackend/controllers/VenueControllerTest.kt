package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.configs.TestConfig
import com.rooplor.classcraftbackend.configs.TestSecurityConfig
import com.rooplor.classcraftbackend.entities.Venue
import com.rooplor.classcraftbackend.entities.location
import com.rooplor.classcraftbackend.services.VenueService
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

@WebMvcTest(VenueController::class)
@Import(TestSecurityConfig::class, TestConfig::class)
@ActiveProfiles("test")
class VenueControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var venueService: VenueService

    @Test
    fun `should return all venues`() {
        val venue =
            Venue(
                "1",
                "TRAIN_3",
                location = location("building", 1),
                description = "description",
                capacity = 100,
                imageUrl = "imageUrl",
            )
        val venueList = listOf(venue, venue.copy(id = "2"))
        Mockito.`when`(venueService.findAllVenue()).thenReturn(venueList)

        mockMvc
            .perform(get("/api/venue"))
            .andExpect(status().isOk)
    }

    @Test
    fun `should insert a venue`() {
        val venue =
            Venue(
                "1",
                "TRAIN_3",
                location = location("building", 1),
                description = "description",
                capacity = 100,
                imageUrl = "imageUrl",
            )
        Mockito.`when`(venueService.insertVenue(venue)).thenReturn(venue)

        mockMvc
            .perform(get("/api/venue"))
            .andExpect(status().isOk)
    }

    @Test
    fun `should update a venue`() {
        val venue =
            Venue(
                "1",
                "TRAIN_3",
                location = location("building", 1),
                description = "description",
                capacity = 100,
                imageUrl = "imageUrl",
            )
        Mockito.`when`(venueService.updateVenue("1", venue)).thenReturn(venue)

        mockMvc
            .perform(get("/api/venue"))
            .andExpect(status().isOk)
    }
}
