package com.rooplor.classcraftbackend.enums

enum class VenueStatus(
    val id: Int,
    val value: String,
) {
    PENDING(1, "Pending"),
    APPROVED(2, "Approved"),
    REJECTED(3, "Rejected"),
}
