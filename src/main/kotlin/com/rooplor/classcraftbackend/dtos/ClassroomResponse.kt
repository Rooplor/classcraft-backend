package com.rooplor.classcraftbackend.dtos

import com.rooplor.classcraftbackend.enums.ClassType
import com.rooplor.classcraftbackend.enums.Format
import com.rooplor.classcraftbackend.enums.Status
import com.rooplor.classcraftbackend.enums.VenueStatus
import com.rooplor.classcraftbackend.types.DateWithVenue
import java.time.LocalDateTime

data class ClassroomResponse(
    var id: String? = null,
    var title: String = "",
    var details: String = "",
    var target: String = "",
    var prerequisite: String? = "",
    var type: ClassType = ClassType.LECTURE,
    var format: Format = Format.ONSITE,
    var capacity: Int = 0,
    var dates: List<DateWithVenue> = emptyList(),
    var stepperStatus: Int? = Status.RESERVE_VENUE.id,
    var meetingUrl: String? = null,
    var content: String? = null,
    var registrationUrl: String? = null,
    var registrationStatus: Boolean? = null,
    var isPublished: Boolean? = null,
    var venueStatus: Int? = VenueStatus.NO_REQUEST.id,
    var rejectReason: String? = "",
    var instructorName: String = "",
    var instructorBio: String = "",
    var instructorAvatar: String = "",
    var instructorFamiliarity: String = "",
    var coverImage: String? = "",
    var createdWhen: LocalDateTime = LocalDateTime.now(),
    var updatedWhen: LocalDateTime = LocalDateTime.now(),
    var owner: String = "",
    var coOwners: List<String>? = emptyList(),
    var classEnrollDetail: ClassEnrollDetail = ClassEnrollDetail(),
)

data class ClassEnrollDetail(
    var enrollBy: List<UserDetailDTO> = emptyList(),
    var isEnrolled: Boolean = false,
    var isApproved: Boolean = false,
)
