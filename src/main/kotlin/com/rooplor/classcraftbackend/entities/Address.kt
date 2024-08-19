package com.rooplor.classcraftbackend.entities

import lombok.Data

@Data
class Address(
    val country: String,
    val city: String,
    val postCode: String,
)
