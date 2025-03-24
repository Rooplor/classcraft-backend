package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.dtos.UserDetailDTO
import com.rooplor.classcraftbackend.entities.ReopenRequest
import com.rooplor.classcraftbackend.entities.RequestDetail
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.ReopenRequestRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ReopenRequestService(
    private val reopenRequestRepository: ReopenRequestRepository,
    private val classroomService: ClassService,
    private val authService: AuthService,
) {
    fun upsertRequest(classroomId: String): ReopenRequest {
        val userId = authService.getUserId()
        val userDetail = authService.getAuthenticatedUserDetails()
        val userDetailDTO =
            UserDetailDTO(
                id = userDetail.id!!,
                username = userDetail.username,
                profilePicture = userDetail.profilePicture,
            )
        val newRequestDetail = RequestDetail(requestedBy = userDetailDTO, requestedAt = LocalDateTime.now())
        val request = reopenRequestRepository.findByClassroomId(classroomId)

        if (request == null) {
            val classroom = classroomService.findClassById(classroomId)
            if (classroom.owner == userId) {
                throw Exception(ErrorMessages.REQUEST_OWNER_CANNOT_REQUEST)
            }
            val newRequest =
                ReopenRequest(
                    classroomId = classroomId,
                    ownerId = classroom.owner,
                    requestList = listOf(newRequestDetail),
                )
            return reopenRequestRepository.save(newRequest)
        } else {
            if (request.requestList.any { it.requestedBy.id == userDetailDTO.id }) {
                throw Exception(ErrorMessages.REQUEST_ALREADY_EXISTS)
            }
            if (request.ownerId == userId) {
                throw Exception(ErrorMessages.REQUEST_OWNER_CANNOT_REQUEST)
            }
            request.requestList = request.requestList.plus(newRequestDetail)
            return reopenRequestRepository.save(request)
        }
    }

    fun getRequestByOwnerId(ownerId: String): List<ReopenRequest> = reopenRequestRepository.findByOwnerId(ownerId)

    fun deleteRequest(classroomId: String) {
        reopenRequestRepository.deleteByClassroomId(classroomId)
    }
}
