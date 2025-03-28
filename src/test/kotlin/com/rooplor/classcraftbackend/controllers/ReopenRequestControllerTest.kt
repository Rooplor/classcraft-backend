package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.configs.TestConfig
import com.rooplor.classcraftbackend.configs.TestSecurityConfig
import com.rooplor.classcraftbackend.entities.ReopenRequest
import com.rooplor.classcraftbackend.services.ReopenRequestService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ReopenRequestController::class)
@Import(TestSecurityConfig::class, TestConfig::class)
@ActiveProfiles("test")
class ReopenRequestControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var reopenRequestService: ReopenRequestService

    @Test
    fun `upsertRequest should return created request`() {
        val classroomId = "class1"
        val reopenRequest = ReopenRequest(classroomId = classroomId, ownerId = "owner1", requestList = emptyList())
        `when`(reopenRequestService.upsertRequest(classroomId)).thenReturn(reopenRequest)

        mockMvc
            .perform(get("/api/request/{classroomId}", classroomId))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result.classroomId").value(classroomId))
    }

    @Test
    fun `getRequestByOwnerId should return list of requests`() {
        val ownerId = "owner1"
        val reopenRequests = listOf(ReopenRequest(classroomId = "class1", ownerId = ownerId, requestList = emptyList()))
        `when`(reopenRequestService.getRequestByOwnerId(ownerId)).thenReturn(reopenRequests)

        mockMvc
            .perform(get("/api/request/owner/{ownerId}", ownerId))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result[0].ownerId").value(ownerId))
    }

    @Test
    fun `deleteRequest should return success message`() {
        val classroomId = "class1"
        doNothing().`when`(reopenRequestService).deleteRequest(classroomId)

        mockMvc
            .perform(delete("/api/request/{classroomId}", classroomId))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result").value("Request deleted"))
    }
}
