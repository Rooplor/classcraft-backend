package com.rooplor.classcraftbackend.types

import com.rooplor.classcraftbackend.enums.AttendeesStatus
import java.time.LocalDate

data class Attendees(
    var day: Number,
    var date: LocalDate,
    var attendeesStatus: AttendeesStatus = AttendeesStatus.PENDING,
)
