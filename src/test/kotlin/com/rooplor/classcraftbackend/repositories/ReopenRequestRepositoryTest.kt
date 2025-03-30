package com.rooplor.classcraftbackend.repositories

import com.rooplor.classcraftbackend.entities.ReopenRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.mockito.Mockito.`when`
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.boot.test.mock.mockito.MockBean
import kotlin.test.Test

@DataMongoTest
class ReopenRequestRepositoryTest {
    @MockBean
    private lateinit var reopenRequestRepository: ReopenRequestRepository

    @Test
    fun `should find ReopenRequest by classroomId`() {
        val classroomId = "class1"
        val reopenRequest = ReopenRequest(classroomId = classroomId, ownerId = "owner1", requestList = emptyList())
        `when`(reopenRequestRepository.save(reopenRequest)).thenReturn(reopenRequest)
        `when`(reopenRequestRepository.findByClassroomId(classroomId)).thenReturn(reopenRequest)

        reopenRequestRepository.save(reopenRequest)
        val foundRequest = reopenRequestRepository.findByClassroomId(classroomId)
        assertNotNull(foundRequest)
        assertEquals(classroomId, foundRequest?.classroomId)
    }

    @Test
    fun `should find ReopenRequest by ownerId`() {
        val ownerId = "owner1"
        val reopenRequest = ReopenRequest(classroomId = "class1", ownerId = ownerId, requestList = emptyList())
        `when`(reopenRequestRepository.save(reopenRequest)).thenReturn(reopenRequest)
        `when`(reopenRequestRepository.findByOwnerId(ownerId)).thenReturn(listOf(reopenRequest))

        reopenRequestRepository.save(reopenRequest)
        val foundRequests = reopenRequestRepository.findByOwnerId(ownerId)
        assertFalse(foundRequests.isEmpty())
        assertEquals(ownerId, foundRequests[0].ownerId)
    }

    @Test
    fun `should delete ReopenRequest by classroomId`() {
        val classroomId = "class1"
        val reopenRequest = ReopenRequest(classroomId = classroomId, ownerId = "owner1", requestList = emptyList())
        reopenRequestRepository.save(reopenRequest)

        reopenRequestRepository.deleteByClassroomId(classroomId)
        val foundRequest = reopenRequestRepository.findByClassroomId(classroomId)
        assertNull(foundRequest)
    }

    @Test
    fun `should check if ReopenRequest exists by classroomId`() {
        val classroomId = "class1"
        val reopenRequest = ReopenRequest(classroomId = classroomId, ownerId = "owner1", requestList = emptyList())
        `when`(reopenRequestRepository.save(reopenRequest)).thenReturn(reopenRequest)
        `when`(reopenRequestRepository.existsByClassroomId(classroomId)).thenReturn(true)

        reopenRequestRepository.save(reopenRequest)
        val exists = reopenRequestRepository.existsByClassroomId(classroomId)
        assertEquals(true, exists)
    }
}
