package com.rooplor.classcraftbackend.dtos

import com.rooplor.classcraftbackend.entities.Venue
import java.time.LocalDateTime

data class DateWithVenueDTO(
    var date: DateDetail = DateDetail(),
    var venue: List<Venue>,
)

data class DateDetail(
    var startDateTime: StartEndDetail = StartEndDetail(),
    var endDateTime: StartEndDetail = StartEndDetail(),
)

data class StartEndDetail(
    var date: String = LocalDateTime.now().toLocalDate().toString(),
    var time: String = LocalDateTime.now().toLocalTime().toString(),
)
