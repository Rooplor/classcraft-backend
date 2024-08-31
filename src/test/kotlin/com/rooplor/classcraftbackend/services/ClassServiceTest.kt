package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.Class
import com.rooplor.classcraftbackend.entities.Venue
import com.rooplor.classcraftbackend.enums.ClassType
import com.rooplor.classcraftbackend.enums.Format
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
    private val venueService: VenueService = Mockito.mock(VenueService::class.java)
    private val classService: ClassService = ClassService(classRepository, venueService)

    @Test
    fun `should return all classes`() {
        val classes =
            listOf(
                Class(
                    title = "React Native",
                    details = "Learn how to build mobile apps with React Native",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    date = listOf(),
                ),
                Class(
                    title = "Spring Boot 101",
                    details = "Learn how to build web apps with Spring Boot",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
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
                title = "React Native",
                details = "Learn how to build mobile apps with React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
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
                title = "React Native",
                details = "Learn how to build mobile apps with React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
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
        val venues =
            Venue(
                id = "1",
                name = "TRAIN_3",
            )
        val classObj =
            Class(
                id = classId,
                title = "React Native",
                details = "Learn how to build mobile apps with React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                date = listOf(),
                venue = venues,
                venueStatus = VenueStatus.PENDING,
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classObj))
        Mockito.`when`(classRepository.save(classObj)).thenReturn(classObj)

        val result = classService.updateVenueClass(classId, "1")
        assertEquals(classObj, result)
    }

    @Test
    fun `should update meeting url of a class`() {
        val classId = "1"
        val meetingUrl = "https://meet.google.com/abc-xyz"
        val classObj =
            Class(
                id = classId,
                title = "React Native",
                details = "Learn how to build mobile apps with React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
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
                title = "React Native",
                details = "Learn how to build mobile apps with React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
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
                title = "React Native",
                details = "Learn how to build mobile apps with React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
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
                title = "React Native",
                details = "Learn how to build mobile apps with React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
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
                title = "React Native",
                details = "Learn how to build mobile apps with React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
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
