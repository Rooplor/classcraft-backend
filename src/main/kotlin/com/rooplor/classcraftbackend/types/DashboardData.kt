package com.rooplor.classcraftbackend.types

import com.rooplor.classcraftbackend.enums.AttendeesStatus
import java.time.LocalDateTime

data class DashboardData(
    val classroomDetails: ClassroomDetails,
    val attendanceSummary: AttendanceSummary,
    val feedbackSummary: FeedbackSummary,
    val formSubmissionsSummary: FormSubmissionsSummary,
    val userEngagement: UserEngagement,
    val classMaterialsSummary: ClassMaterialsSummary,
)

data class ClassroomDetails(
    val title: String,
    val description: String,
    val instructorName: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val numberOfSessions: Int,
    val venueDetails: List<VenueDetails>,
    val viewCount: Int,
)

data class VenueDetails(
    val room: String,
    val building: String,
    val floor: Int,
    val capacity: Int,
)

data class AttendanceSummary(
    val totalAttendees: Int,
    val attendanceRate: Double,
    val attendees: List<Attendee>,
)

data class Attendee(
    val userId: String,
    val userName: String,
    val attendanceStatus: List<AttendeesStatus>,
)

data class FeedbackSummary(
    val averageScores: Map<String, Double>,
    val commonComments: List<String>,
    val responseRate: Double,
)

data class FormSubmissionsSummary(
    val totalSubmissions: Int,
    val responsesSummary: Map<String, Any>,
)

data class UserEngagement(
    val totalUsers: Int,
    val activeUsers: Int,
)

data class ClassMaterialsSummary(
    val materials: List<String>,
    val accessStatistics: Map<String, Int>,
)
