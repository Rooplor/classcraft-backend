package com.rooplor.classcraftbackend.dtos

import com.rooplor.classcraftbackend.enums.Format
import com.rooplor.classcraftbackend.enums.Type
import java.time.LocalDateTime

data class InitClassDTO(
    var title: String,
    var details: String,
    var target: String,
    var prerequisite: String,
    var type: Type,
    var format: Format,
    var capacity: Int,
    var date: List<LocalDateTime>,
)
