package com.rooplor.classcraftbackend.enums

enum class VenueStatus(
    val id: Int,
    val value: String,
) {
    NO_REQUEST(1, "No Request"),
    PENDING(2, "Pending"),
    APPROVED(3, "Approved"),
    REJECTED(4, "Rejected"),
}
