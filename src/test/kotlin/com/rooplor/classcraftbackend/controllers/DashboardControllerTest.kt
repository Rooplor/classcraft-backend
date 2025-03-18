package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.configs.TestConfig
import com.rooplor.classcraftbackend.configs.TestSecurityConfig
import com.rooplor.classcraftbackend.services.DashboardService
import com.rooplor.classcraftbackend.types.AttendanceSummary
import com.rooplor.classcraftbackend.types.ClassMaterialsSummary
import com.rooplor.classcraftbackend.types.ClassroomDetails
import com.rooplor.classcraftbackend.types.DashboardData
import com.rooplor.classcraftbackend.types.FeedbackSummary
import com.rooplor.classcraftbackend.types.FormSubmissionsSummary
import com.rooplor.classcraftbackend.types.UserEngagement
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebMvcTest(DashboardController::class)
@Import(TestSecurityConfig::class, TestConfig::class)
@ActiveProfiles("test")
class DashboardControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var dashboardService: DashboardService

    @Test
    fun `getDashboardData should return dashboard data`() {
        val classroomId = "classroomId"
        val dashboardData =
            DashboardData(
                classroomDetails =
                    ClassroomDetails(
                        title = "Test Classroom",
                        description = "Details",
                        instructorName = "Instructor",
                        startDate = LocalDateTime.now(),
                        endDate = LocalDateTime.now(),
                        numberOfSessions = 10,
                        venueDetails = emptyList(),
                        viewCount = 100,
                    ),
                attendanceSummary = AttendanceSummary(10, 80.0, emptyList()),
                feedbackSummary = FeedbackSummary(emptyMap(), emptyList(), 0.0),
                formSubmissionsSummary = FormSubmissionsSummary(10, emptyMap()),
                userEngagement = UserEngagement(10, 5),
                classMaterialsSummary = ClassMaterialsSummary(emptyList(), emptyMap()),
            )

        `when`(dashboardService.getDashboardData(classroomId)).thenReturn(dashboardData)

        mockMvc
            .perform(get("/api/dashboard/$classroomId"))
            .andExpect(status().isOk)
            .andExpect(
                content().json(
                    """
                    {
                        "success": true,
                        "result": {
                            "classroomDetails": {
                                "title": "Test Classroom",
                                "description": "Details",
                                "instructorName": "Instructor",
                                "startDate": "${dashboardData.classroomDetails.startDate}",
                                "endDate": "${dashboardData.classroomDetails.endDate}",
                                "numberOfSessions": 10,
                                "venueDetails": [],
                                "viewCount": 100
                            },
                            "attendanceSummary": {
                                "totalAttendees": 10,
                                "attendanceRate": 80.0,
                                "attendees": []
                            },
                            "feedbackSummary": {
                                "averageScores": {},
                                "commonComments": [],
                                "responseRate": 0.0
                            },
                            "formSubmissionsSummary": {
                                "totalSubmissions": 10,
                                "responsesSummary": {}
                            },
                            "userEngagement": {
                                "totalUsers": 10,
                                "activeUsers": 5
                            },
                            "classMaterialsSummary": {
                                "materials": [],
                                "accessStatistics": {}
                            }
                        },
                        "error": null
                    }
                    """.trimIndent(),
                ),
            )
    }
}
