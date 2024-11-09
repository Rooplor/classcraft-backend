package com.rooplor.classcraftbackend.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class ReservationDTO(
    @JsonProperty("venueId")
    var venueId: List<String>,
)
