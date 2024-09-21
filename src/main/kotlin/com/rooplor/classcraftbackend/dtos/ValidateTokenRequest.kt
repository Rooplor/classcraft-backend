package com.rooplor.classcraftbackend.dtos

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ValidateTokenRequest
    @JsonCreator
    constructor(
        @JsonProperty("idToken") val idToken: String,
    )
