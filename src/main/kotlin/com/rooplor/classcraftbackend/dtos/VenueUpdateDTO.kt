package com.rooplor.classcraftbackend.dtos

import com.rooplor.classcraftbackend.enums.Venue
import com.rooplor.classcraftbackend.enums.VenueStatus

class VenueUpdateDTO(
    var venue: Venue,
    var status: VenueStatus,
)
