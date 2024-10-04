package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.configs.TestConfig
import com.rooplor.classcraftbackend.configs.TestSecurityConfig
import com.rooplor.classcraftbackend.entities.Venue
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
        val venue = listOf(Venue("1", "TRAIN_3"), Venue("2", "TRAIN_4"))
        Mockito.`when`(venueService.findAllVenue()).thenReturn(venue)

        mockMvc
            .perform(get("/api/venue"))
            .andExpect(status().isOk)
    }

    @Test
    fun `should insert a venue`() {
        val venue = Venue("1", "TRAIN_3")
        Mockito.`when`(venueService.insertVenue(venue)).thenReturn(venue)

        mockMvc
            .perform(get("/api/venue"))
            .andExpect(status().isOk)
    }
}
