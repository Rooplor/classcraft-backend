package com.rooplor.classcraftbackend.entities

import com.rooplor.classcraftbackend.dtos.UserDetailDTO
import org.springframework.data.annotation.Id
import java.time.LocalDateTime

data class ReopenRequest(
    @Id
    var id: String? = null,
    var classroomId: String = "",
    var ownerId: String = "",
    var requestList: List<RequestDetail> = emptyList(),
)

data class RequestDetail(
    var requestedBy: UserDetailDTO = UserDetailDTO(),
    var requestedAt: LocalDateTime = LocalDateTime.now(),
)
