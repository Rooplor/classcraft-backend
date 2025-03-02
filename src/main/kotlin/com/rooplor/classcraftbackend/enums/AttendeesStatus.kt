package com.rooplor.classcraftbackend.enums

enum class AttendeesStatus(
    val id: Int,
    val value: String,
) {
    PRESENT(1, "Present"),
    ABSENT(2, "Absent"),
    LATE(3, "Late"),
    NOT_GOING(4, "Not Going"),
    PENDING(5, "Pending"),
}
