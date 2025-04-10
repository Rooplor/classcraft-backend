package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.Classroom
import com.rooplor.classcraftbackend.entities.FormSubmission
import com.rooplor.classcraftbackend.entities.User
import com.rooplor.classcraftbackend.enums.AttendeesStatus
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.ClassroomRepository
import com.rooplor.classcraftbackend.repositories.FormSubmissionRepository
import com.rooplor.classcraftbackend.repositories.UserRepository
import com.rooplor.classcraftbackend.repositories.VenueRepository
import com.rooplor.classcraftbackend.types.AttendanceSummary
import com.rooplor.classcraftbackend.types.Attendee
import com.rooplor.classcraftbackend.types.ClassMaterialsSummary
import com.rooplor.classcraftbackend.types.ClassroomDetails
import com.rooplor.classcraftbackend.types.DashboardData
import com.rooplor.classcraftbackend.types.FeedbackSummary
import com.rooplor.classcraftbackend.types.FormSubmissionsSummary
import com.rooplor.classcraftbackend.types.UserEngagement
import com.rooplor.classcraftbackend.types.VenueDetails
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DashboardService(
    private val classroomRepository: ClassroomRepository,
    private val formSubmissionRepository: FormSubmissionRepository,
    private val userRepository: UserRepository,
    private val venueRepository: VenueRepository,
    private val authService: AuthService,
) {
    fun getDashboardData(classroomId: String): DashboardData {
        val userId = authService.getUserId()
        val classroom = classroomRepository.findById(classroomId).orElseThrow { Exception(ErrorMessages.CLASS_NOT_FOUND) }
        if (classroom.owner != userId) {
            throw Exception(ErrorMessages.OWNER_NOT_MATCH)
        }
        val formSubmissions = formSubmissionRepository.findByClassroomId(classroomId)
        val usersIdInClass = formSubmissions.mapNotNull { it.submittedBy }.distinct()
        val usersInClass = userRepository.findAllById(usersIdInClass)

        return DashboardData(
            classroomDetails = getClassroomDetails(classroom),
            attendanceSummary = getAttendanceSummary(formSubmissions),
            feedbackSummary = getFeedbackSummary(formSubmissions),
            formSubmissionsSummary = getFormSubmissionsSummary(formSubmissions),
            userEngagement = getUserEngagement(usersInClass, formSubmissions),
            classMaterialsSummary = getClassMaterialsSummary(classroom),
        )
    }

    private fun getClassroomDetails(classroom: Classroom): ClassroomDetails {
        val venues =
            classroom.dates.flatMap { it.venueId }.distinct().map { venueId ->
                venueRepository.findById(venueId).orElseThrow { Exception(ErrorMessages.VENUE_NOT_FOUND) }
            }
        val venueDetails =
            venues.map { venue ->
                VenueDetails(
                    room = venue.room,
                    building = venue.location.building,
                    floor = venue.location.floor.toInt(),
                    capacity = venue.capacity.toInt(),
                )
            }
        val startDate = classroom.dates.minOfOrNull { it.date.startDateTime } ?: LocalDateTime.now()
        val endDate = classroom.dates.maxOfOrNull { it.date.endDateTime } ?: LocalDateTime.now()
        return ClassroomDetails(
            title = classroom.title,
            description = classroom.details,
            instructorName = classroom.instructorName,
            startDate = startDate,
            endDate = endDate,
            numberOfSessions = classroom.dates.size,
            venueDetails = venueDetails,
            viewCount = classroom.viewCount,
        )
    }

    private fun getAttendanceSummary(formSubmissions: List<FormSubmission>): AttendanceSummary {
        val totalApprovedSubmissions = formSubmissions.count { it.isApprovedByOwner }
        val totalAttendees =
            formSubmissions.count {
                it.isApprovedByOwner && it.attendeesStatus?.any { status -> status.attendeesStatus == AttendeesStatus.PRESENT } == true
            }
        val attendanceRate =
            if (totalApprovedSubmissions > 0) {
                totalAttendees.toDouble() / totalApprovedSubmissions * 100
            } else {
                0.0
            }
        val attendees =
            formSubmissions.map {
                Attendee(
                    userId = it.submittedBy ?: "",
                    userName = it.userDetail?.username ?: "",
                    attendanceStatus = it.attendeesStatus?.map { status -> status.attendeesStatus } ?: emptyList(),
                )
            }
        return AttendanceSummary(totalAttendees, attendanceRate, attendees)
    }

    private fun getFeedbackSummary(formSubmissions: List<FormSubmission>): FeedbackSummary {
        val feedbackResponses = formSubmissions.mapNotNull { it.feedbackResponse }

        val averageScores =
            mutableMapOf(
                "Rating" to feedbackResponses.mapNotNull { it["Rating"] as? Double }.average(),
            )
        if (averageScores["Rating"]!!.isNaN()) {
            averageScores["Rating"] = 0.0
        }

        val commonComments = feedbackResponses.mapNotNull { it["Comments"] as? String }

        val responseRate = feedbackResponses.size.toDouble() / formSubmissions.size * 100

        return FeedbackSummary(averageScores, commonComments, responseRate)
    }

    private fun getFormSubmissionsSummary(formSubmissions: List<FormSubmission>): FormSubmissionsSummary {
        val totalSubmissions = formSubmissions.size
        val responsesSummary =
            formSubmissions
                .flatMap { it.responses.entries }
                .groupBy({ it.key }, { it.value })
                .mapValues { it.value.groupingBy { value -> value }.eachCount() }
        return FormSubmissionsSummary(totalSubmissions, responsesSummary)
    }

    private fun getUserEngagement(
        users: List<User>,
        formSubmissions: List<FormSubmission>,
    ): UserEngagement {
        val totalUsers = users.size
        val activeUsers = formSubmissions.mapNotNull { it.submittedBy }.distinct().size
        return UserEngagement(totalUsers, activeUsers)
    }

    private fun getClassMaterialsSummary(classroom: Classroom): ClassMaterialsSummary {
        val materials = classroom.classMaterials ?: emptyList()
        val accessStatistics = materials.associateWith { 0 }
        return ClassMaterialsSummary(materials, accessStatistics)
    }
}
