package com.rooplor.classcraftbackend.enums

enum class Status(
    val id: Int,
    val value: String,
) {
    FILL_CLASS_DETAIL(1, "Fill Class Detail"),
    RESERVE_VENUE(2, "Reserve Venue"),
    CRAFT_CONTENT(3, "Craft Content"),
    PREPARE_FOR_REG(4, "Prepare for Registration"),
}
