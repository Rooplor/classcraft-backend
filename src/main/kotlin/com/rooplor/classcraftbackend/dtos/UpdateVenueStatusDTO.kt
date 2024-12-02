package com.rooplor.classcraftbackend.dtos

data class UpdateVenueStatusDTO(
    val venueStatusId: Int,
    val rejectReason: String? = "",
)
