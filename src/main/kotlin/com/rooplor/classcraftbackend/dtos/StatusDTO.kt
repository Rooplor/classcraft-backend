package com.rooplor.classcraftbackend.dtos

import lombok.Getter
import lombok.Setter

@Getter
@Setter
data class StatusDTO(
    var status: Boolean = false
)
