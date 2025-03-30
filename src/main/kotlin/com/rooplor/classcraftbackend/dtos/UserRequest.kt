package com.rooplor.classcraftbackend.dtos

import lombok.NoArgsConstructor

@NoArgsConstructor
data class UserRequest(
    var username: String = "",
    var email: String = "",
    var profilePicture: String? = null,
    var bio: String? = null,
)
