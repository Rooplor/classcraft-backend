package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.Classroom
import com.rooplor.classcraftbackend.entities.FormSubmission
import com.rooplor.classcraftbackend.entities.User
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.ClassroomRepository
import com.rooplor.classcraftbackend.repositories.FormSubmissionRepository
import com.rooplor.classcraftbackend.repositories.UserRepository
import com.rooplor.classcraftbackend.repositories.VenueRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import java.util.Optional

@SpringBootTest
class DashboardServiceTest {
    @Mock
    private lateinit var classroomRepository: ClassroomRepository

    @Mock
    private lateinit var formSubmissionRepository: FormSubmissionRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var venueRepository: VenueRepository

    @Mock
    private lateinit var authService: AuthService

    @InjectMocks
    private lateinit var dashboardService: DashboardService

    @Test
    fun `getDashboardData should return dashboard data`() {
        val classroomId = "classroomId"
        val classroom =
            Classroom(
                id = classroomId,
                title = "title",
                details = "details",
                instructorName = "instructorName",
                dates = emptyList(),
                owner = "owner",
                viewCount = 0,
            )
        val formSubmissions = emptyList<FormSubmission>()
        val users = emptyList<User>()

        `when`(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom))
        `when`(formSubmissionRepository.findByClassroomId(classroomId)).thenReturn(formSubmissions)
        `when`(userRepository.findAllById(any())).thenReturn(users)
        `when`(authService.getUserId()).thenReturn("owner")

        val result = dashboardService.getDashboardData(classroomId)

        assertEquals(classroom.title, result.classroomDetails.title)
        assertEquals(classroom.details, result.classroomDetails.description)
        assertEquals(classroom.instructorName, result.classroomDetails.instructorName)
        assertEquals(classroom.viewCount, result.classroomDetails.viewCount)

        assertEquals(0, result.attendanceSummary.totalAttendees)
        assertEquals(0.0, result.attendanceSummary.attendanceRate)
        assertEquals(0, result.attendanceSummary.attendees.size)

        assertEquals(emptyList<String>(), result.feedbackSummary.commonComments)

        assertEquals(0, result.formSubmissionsSummary.totalSubmissions)

        assertEquals(0, result.userEngagement.totalUsers)
        assertEquals(0, result.userEngagement.activeUsers)

        assertEquals(emptyList<String>(), result.classMaterialsSummary.materials)
    }

    @Test
    fun `getDashboardData should throw owner not match when user that get dashboard is not a owner of this classroom`() {
        val classroomId = "classroomId"
        val classroom =
            Classroom(
                id = classroomId,
                title = "title",
                details = "details",
                instructorName = "instructorName",
                dates = emptyList(),
                owner = "owner1",
                viewCount = 0,
            )
        val formSubmissions = emptyList<FormSubmission>()
        val users = emptyList<User>()

        `when`(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom))
        `when`(formSubmissionRepository.findByClassroomId(classroomId)).thenReturn(formSubmissions)
        `when`(userRepository.findAllById(any())).thenReturn(users)
        `when`(authService.getUserId()).thenReturn("owner2")

        try {
            dashboardService.getDashboardData(classroomId)
        } catch (e: Exception) {
            assertEquals(ErrorMessages.OWNER_NOT_MATCH, e.message)
        }
    }
}
