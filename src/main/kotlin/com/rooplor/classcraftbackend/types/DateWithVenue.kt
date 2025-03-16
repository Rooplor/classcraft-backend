package com.rooplor.classcraftbackend.types

import java.time.LocalDateTime

data class DateWithVenue(
    var date: DateDetail = DateDetail(),
    var venueId: List<String>,
)

data class DateDetail(
    var startDateTime: LocalDateTime = LocalDateTime.now(),
    var endDateTime: LocalDateTime = LocalDateTime.now(),
)
