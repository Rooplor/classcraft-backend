package com.rooplor.classcraftbackend.entities

import com.rooplor.classcraftbackend.dtos.ClassroomDetail
import com.rooplor.classcraftbackend.dtos.UserDetailDTO
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "reopenRequest")
data class ReopenRequest(
    @Id
    var id: String? = null,
    var classroomId: String = "",
    var classroomDetail: ClassroomDetail = ClassroomDetail(),
    var ownerId: String = "",
    var requestList: List<RequestDetail> = emptyList(),
)

data class RequestDetail(
    var requestedBy: UserDetailDTO = UserDetailDTO(),
    var requestedAt: LocalDateTime = LocalDateTime.now(),
)
