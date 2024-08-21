package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.dtos.VenueUpdateDTO
import com.rooplor.classcraftbackend.entities.Class
import com.rooplor.classcraftbackend.enums.Format
import com.rooplor.classcraftbackend.enums.Type
import com.rooplor.classcraftbackend.enums.Venue
import com.rooplor.classcraftbackend.enums.VenueStatus
import com.rooplor.classcraftbackend.repositories.ClassRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import java.util.Optional

@SpringBootTest
class ClassServiceTest {
    private val classRepository: ClassRepository = Mockito.mock(ClassRepository::class.java)
    private val classService: ClassService = ClassService(classRepository)

    @Test
    fun `should return all classes`() {
        val classes =
            listOf(
                Class(
                    title = "Test Class",
                    details = "Details",
                    target = "Target",
                    prerequisite = "None",
                    type = Type.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    date = listOf(),
                ),
                Class(
                    title = "Test Class 2",
                    details = "Details 2",
                    target = "Target 2",
                    prerequisite = "None",
                    type = Type.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    date = listOf(),
                ),
            )
        Mockito.`when`(classRepository.findAll()).thenReturn(classes)

        val result = classService.findAllClass()
        assertEquals(classes, result)
    }

    @Test
    fun `should return class by id`() {
        val classId = "1"
        val classObj =
            Class(
                id = classId,
                title = "Test Class",
                details = "Details",
                target = "Target",
                prerequisite = "None",
                type = Type.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                date = listOf(),
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classObj))

        val result = classService.findClassById(classId)
        assertEquals(classObj, result)
    }

    @Test
    fun `should insert a new class`() {
        val classObj =
            Class(
                title = "Test Class",
                details = "Details",
                target = "Target",
                prerequisite = "None",
                type = Type.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                date = listOf(),
            )
        Mockito.`when`(classRepository.insert(classObj)).thenReturn(classObj)

        val result = classService.insertClass(classObj)
        assertEquals(classObj, result)
    }

    @Test
    fun `should update venue of a class`() {
        val classId = "1"
        val venueUpdateDTO = VenueUpdateDTO(Venue.TRAIN_3, VenueStatus.PENDING)
        val classObj =
            Class(
                id = classId,
                title = "Test Class",
                details = "Details",
                target = "Target",
                prerequisite = "None",
                type = Type.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                date = listOf(),
                venue = Venue.TRAIN_3,
                venueStatus = VenueStatus.PENDING,
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classObj))
        Mockito.`when`(classRepository.save(classObj)).thenReturn(classObj)

        val result = classService.updateVenueClass(classId, venueUpdateDTO)
        assertEquals(classObj, result)
    }

    @Test
    fun `should update meeting url of a class`() {
        val classId = "1"
        val meetingUrl = "https://meet.google.com/abc-xyz"
        val classObj =
            Class(
                id = classId,
                title = "Test Class",
                details = "Details",
                target = "Target",
                prerequisite = "None",
                type = Type.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                date = listOf(),
                meetingUrl = "https://meet.google.com/abc-xyz",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classObj))
        Mockito.`when`(classRepository.save(classObj)).thenReturn(classObj)

        val result = classService.updateMeetingUrlClass(classId, meetingUrl)
        assertEquals(classObj, result)
    }

    @Test
    fun `should update content of a class`() {
        val classId = "1"
        val classContent = "{\"key\": \"value\"}"
        val classObj =
            Class(
                id = classId,
                title = "Test Class",
                details = "Details",
                target = "Target",
                prerequisite = "None",
                type = Type.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                date = listOf(),
                content = "{\"key\": \"value\"}",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classObj))
        Mockito.`when`(classRepository.save(classObj)).thenReturn(classObj)

        val result = classService.updateContent(classId, classContent)
        assertEquals(classObj, result)
    }

    @Test
    fun `should update registration url of a class`() {
        val classId = "1"
        val registrationUrl = "https://example.com/register"
        val classObj =
            Class(
                id = classId,
                title = "Test Class",
                details = "Details",
                target = "Target",
                prerequisite = "None",
                type = Type.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                date = listOf(),
                registrationUrl = "https://example.com/register",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classObj))
        Mockito.`when`(classRepository.save(classObj)).thenReturn(classObj)

        val result = classService.updateRegistrationUrl(classId, registrationUrl)
        assertEquals(classObj, result)
    }

    @Test
    fun `should toggle registration status of a class`() {
        val classId = "1"
        val classObj =
            Class(
                id = classId,
                title = "Test Class",
                details = "Details",
                target = "Target",
                prerequisite = "None",
                type = Type.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                date = listOf(),
                registrationStatus = false,
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classObj))
        Mockito.`when`(classRepository.save(classObj)).thenReturn(classObj)

        val result = classService.toggleRegistrationStatus(classId)
        assertEquals(classObj, result)
    }

    @Test
    fun `should toggle publish status of a class`() {
        val classId = "1"
        val classObj =
            Class(
                id = classId,
                title = "Test Class",
                details = "Details",
                target = "Target",
                prerequisite = "None",
                type = Type.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                date = listOf(),
                isPublished = false,
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classObj))
        Mockito.`when`(classRepository.save(classObj)).thenReturn(classObj)

        val result = classService.togglePublishStatus(classId)
        assertEquals(classObj, result)
    }
}
