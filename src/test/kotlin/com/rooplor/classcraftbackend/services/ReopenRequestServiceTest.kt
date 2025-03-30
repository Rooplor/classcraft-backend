package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.dtos.ClassroomDetail
import com.rooplor.classcraftbackend.dtos.UserDetailDTO
import com.rooplor.classcraftbackend.entities.Classroom
import com.rooplor.classcraftbackend.entities.ReopenRequest
import com.rooplor.classcraftbackend.entities.RequestDetail
import com.rooplor.classcraftbackend.entities.User
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.ReopenRequestRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.any
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import java.time.LocalDateTime
import kotlin.test.Test

class ReopenRequestServiceTest {
    private val reopenRequestRepository = mock(ReopenRequestRepository::class.java)
    private val classroomService = mock(ClassService::class.java)
    private val authService = mock(AuthService::class.java)
    private val reopenRequestService = ReopenRequestService(reopenRequestRepository, classroomService, authService)

    @Test
    fun `upsertRequest should create new request if none exists`() {
        val classroomId = "class1"
        val userId = "user1"
        val user = User(id = userId, email = "mail@mail.com", username = "user1", profilePicture = "profilePic")
        val classroom = Classroom(id = classroomId, owner = "owner1")

        `when`(authService.getUserId()).thenReturn(userId)
        `when`(authService.getAuthenticatedUserDetails()).thenReturn(user)
        `when`(reopenRequestRepository.findByClassroomId(classroomId)).thenReturn(null)
        `when`(classroomService.findClassById(classroomId)).thenReturn(classroom)
        `when`(reopenRequestRepository.save(any(ReopenRequest::class.java))).thenAnswer { it.arguments[0] }

        val result = reopenRequestService.upsertRequest(classroomId)

        assertNotNull(result)
        assertEquals(classroomId, result.classroomId)
        assertEquals(classroom.owner, result.ownerId)
        assertEquals(1, result.requestList.size)
        assertEquals(userId, result.requestList[0].requestedBy.id)
    }

    @Test
    fun `upsertRequest should throw exception if owner tries to request`() {
        val classroomId = "class1"
        val userId = "owner1"
        val user = User(id = userId, email = "mail@mail.com", username = "user1", profilePicture = "profilePic")
        val classroom = Classroom(id = classroomId, owner = userId)

        `when`(authService.getUserId()).thenReturn(userId)
        `when`(authService.getAuthenticatedUserDetails()).thenReturn(user)
        `when`(reopenRequestRepository.findByClassroomId(classroomId)).thenReturn(null)
        `when`(classroomService.findClassById(classroomId)).thenReturn(classroom)

        val exception =
            assertThrows<Exception> {
                reopenRequestService.upsertRequest(classroomId)
            }

        assertEquals(ErrorMessages.REQUEST_OWNER_CANNOT_REQUEST, exception.message)
    }

    @Test
    fun `upsertRequest should update existing request`() {
        val classroomId = "class1"
        val userId = "user1"
        val user = User(id = userId, email = "mail@mail.com", username = "user1", profilePicture = "profilePic")
        val classroomDetail = ClassroomDetail(coverImage = "cover1", title = "title1")
        val existingRequest =
            ReopenRequest(classroomId = "class1", classroomDetail = classroomDetail, ownerId = "owner1", requestList = emptyList())

        `when`(authService.getUserId()).thenReturn(userId)
        `when`(authService.getAuthenticatedUserDetails()).thenReturn(user)
        `when`(reopenRequestRepository.findByClassroomId(classroomId)).thenReturn(existingRequest)
        `when`(reopenRequestRepository.save(any(ReopenRequest::class.java))).thenAnswer { it.arguments[0] }

        val result = reopenRequestService.upsertRequest(classroomId)

        assertNotNull(result)
        assertEquals(classroomId, result.classroomId)
        assertEquals(1, result.requestList.size)
        assertEquals(userId, result.requestList[0].requestedBy.id)
    }

    @Test
    fun `getRequestByOwnerId should return list of requests`() {
        val ownerId = "owner1"
        val classroomDetail = ClassroomDetail(coverImage = "cover1", title = "title1")
        val requests = listOf(ReopenRequest(classroomDetail = classroomDetail, ownerId = ownerId, requestList = emptyList()))

        `when`(authService.getUserId()).thenReturn(ownerId)
        `when`(reopenRequestRepository.findByOwnerId(ownerId)).thenReturn(requests)

        val result = reopenRequestService.getRequestByOwnerId()

        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals(ownerId, result[0].ownerId)
    }

    @Test
    fun `deleteRequest should delete request by classroomId`() {
        val classroomId = "class1"

        doNothing().`when`(reopenRequestRepository).deleteByClassroomId(classroomId)

        reopenRequestService.deleteRequest(classroomId)

        verify(reopenRequestRepository, times(1)).deleteByClassroomId(classroomId)
    }

    @Test
    fun `requestExists should return true if request exists`() {
        val classroomId = "class1"
        val requestList =
            listOf(
                RequestDetail(
                    requestedBy = UserDetailDTO(id = "user1", username = "user1", profilePicture = "profilePic"),
                    requestedAt = LocalDateTime.now(),
                ),
            )
        val request =
            ReopenRequest(
                classroomId = classroomId,
                classroomDetail = ClassroomDetail(coverImage = "cover1", title = "title1"),
                ownerId = "owner1",
                requestList = requestList,
            )
        `when`(authService.getUserId()).thenReturn("user1")
        `when`(reopenRequestRepository.findByClassroomId(classroomId)).thenReturn(request)
        val result = reopenRequestService.requestExists(classroomId)
        assertEquals(true, result)
    }

    @Test
    fun `requestExists should return false if request does not exist`() {
        val classroomId = "class1"
        val requestList =
            listOf(
                RequestDetail(
                    requestedBy = UserDetailDTO(id = "user2", username = "user1", profilePicture = "profilePic"),
                    requestedAt = LocalDateTime.now(),
                ),
            )
        val request =
            ReopenRequest(
                classroomId = classroomId,
                classroomDetail = ClassroomDetail(coverImage = "cover1", title = "title1"),
                ownerId = "owner1",
                requestList = requestList,
            )
        `when`(authService.getUserId()).thenReturn("user1")
        `when`(reopenRequestRepository.findByClassroomId(classroomId)).thenReturn(request)
        val result = reopenRequestService.requestExists(classroomId)
        assertEquals(false, result)
    }

    @Test
    fun `getRequestByByUserId should return list of requests`() {
        val userId = "user1"
        val classroomDetail = ClassroomDetail(coverImage = "cover1", title = "title1")
        val requests =
            listOf(
                ReopenRequest(
                    classroomId = "class1",
                    classroomDetail = classroomDetail,
                    ownerId = "owner1",
                    requestList =
                        listOf(
                            RequestDetail(
                                requestedBy = UserDetailDTO(id = userId, username = "user1", profilePicture = "profilePic"),
                                requestedAt = LocalDateTime.now(),
                            ),
                        ),
                ),
            )

        `when`(authService.getUserId()).thenReturn(userId)
        `when`(reopenRequestRepository.findAll()).thenReturn(requests)

        val result = reopenRequestService.getRequestByByUserId()

        assertNotNull(result)
        assertEquals(1, result.size)
        assertEquals("class1", result[0].classroomId)
    }
}
