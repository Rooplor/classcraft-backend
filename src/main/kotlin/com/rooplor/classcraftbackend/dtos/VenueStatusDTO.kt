package com.rooplor.classcraftbackend.dtos

data class VenueStatusDTO(
    val venueStatusId: Int,
    val rejectReason: String? = "",
)
