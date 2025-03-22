package com.rooplor.classcraftbackend.types

import com.rooplor.classcraftbackend.enums.AttendeesStatus
import java.time.LocalDate
import java.time.LocalDateTime

data class Attendees(
    var day: Int,
    var date: LocalDate,
    var attendeesStatus: AttendeesStatus = AttendeesStatus.PENDING,
    var checkInDateTime: LocalDateTime? = null,
)
