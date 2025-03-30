package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.configs.TestConfig
import com.rooplor.classcraftbackend.configs.TestSecurityConfig
import com.rooplor.classcraftbackend.dtos.ClassroomDetail
import com.rooplor.classcraftbackend.dtos.UserDetailDTO
import com.rooplor.classcraftbackend.entities.ReopenRequest
import com.rooplor.classcraftbackend.entities.RequestDetail
import com.rooplor.classcraftbackend.enums.ClassType
import com.rooplor.classcraftbackend.enums.Format
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
import java.time.LocalDateTime

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
        val classroomDetail =
            ClassroomDetail(
                coverImage = "cover1",
                title = "title1",
                format = Format.ONSITE,
                type = ClassType.LECTURE,
                capacity = 100,
                instructorName = "instructor1",
                instructorAvatar = "avatar1",
            )
        val reopenRequest =
            ReopenRequest(
                classroomId = classroomId,
                classroomDetail = classroomDetail,
                ownerId = "owner1",
                requestList = emptyList(),
            )
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
        val classroomDetail =
            ClassroomDetail(
                coverImage = "cover1",
                title = "title1",
                format = Format.ONSITE,
                type = ClassType.LECTURE,
                capacity = 100,
                instructorName = "instructor1",
                instructorAvatar = "avatar1",
            )
        val reopenRequests =
            listOf(ReopenRequest(classroomId = "class1", classroomDetail = classroomDetail, ownerId = ownerId, requestList = emptyList()))
        `when`(reopenRequestService.getRequestByOwnerId()).thenReturn(reopenRequests)

        mockMvc
            .perform(get("/api/request/owner", ownerId))
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

    @Test
    fun `requestExists should return true if request exists`() {
        val classroomId = "class1"
        `when`(reopenRequestService.requestExists(classroomId)).thenReturn(true)

        mockMvc
            .perform(get("/api/request/exists/{classroomId}", classroomId))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result").value(true))
    }

    @Test
    fun `requestExists should return false if request does not exist`() {
        val classroomId = "class1"
        `when`(reopenRequestService.requestExists(classroomId)).thenReturn(false)

        mockMvc
            .perform(get("/api/request/exists/{classroomId}", classroomId))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result").value(false))
    }

    @Test
    fun `getMyRequests should return list of requests`() {
        val userId = "user1"
        val classroomDetail =
            ClassroomDetail(
                coverImage = "cover1",
                title = "title1",
                format = Format.ONSITE,
                type = ClassType.LECTURE,
                capacity = 100,
                instructorName = "instructor1",
                instructorAvatar = "avatar1",
            )
        val reopenRequests =
            listOf(
                ReopenRequest(
                    classroomId = "class1",
                    classroomDetail = classroomDetail,
                    ownerId = "owner1",
                    requestList =
                        listOf(
                            RequestDetail(
                                requestedBy = UserDetailDTO(id = userId, username = "user1", profilePicture = "profile1"),
                                requestedAt = LocalDateTime.now(),
                            ),
                        ),
                ),
            )
        `when`(reopenRequestService.getRequestByByUserId()).thenReturn(reopenRequests)

        mockMvc
            .perform(get("/api/request/my-requests"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.result[0].requestList[0].requestedBy.id").value(userId))
    }
}
