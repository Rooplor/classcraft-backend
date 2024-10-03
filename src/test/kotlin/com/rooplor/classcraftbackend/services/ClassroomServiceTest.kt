package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.Classroom
import com.rooplor.classcraftbackend.entities.Venue
import com.rooplor.classcraftbackend.enums.ClassType
import com.rooplor.classcraftbackend.enums.Format
import com.rooplor.classcraftbackend.enums.VenueStatus
import com.rooplor.classcraftbackend.repositories.ClassroomRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import java.util.Optional

@SpringBootTest
class ClassroomServiceTest {
    private val classRepository: ClassroomRepository = Mockito.mock(ClassroomRepository::class.java)
    private val venueService: VenueService = Mockito.mock(VenueService::class.java)
    private val classService: ClassService = ClassService(classRepository, venueService)

    @Test
    fun `should return all classes is published`() {
        val classrooms =
            listOf(
                Classroom(
                    title = "React Native",
                    details = "Learn how to build mobile apps with React Native",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    isPublished = true,
                    registrationStatus = true,
                    date = listOf(),
                ),
                Classroom(
                    title = "Spring Boot 101",
                    details = "Learn how to build web apps with Spring Boot",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    isPublished = true,
                    registrationStatus = true,
                    date = listOf(),
                ),
            )
        Mockito.`when`(classRepository.findByRegistrationStatusAndIsPublishedTrue(true)).thenReturn(classrooms)

        val result = classService.findAllClass(true)
        assertEquals(classrooms, result)
    }

    @Test
    fun `should return all classes is published and registrationStatus is false`() {
        val classrooms =
            listOf(
                Classroom(
                    title = "React Native",
                    details = "Learn how to build mobile apps with React Native",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    isPublished = true,
                    registrationStatus = false,
                    date = listOf(),
                ),
                Classroom(
                    title = "Spring Boot 101",
                    details = "Learn how to build web apps with Spring Boot",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    isPublished = true,
                    registrationStatus = false,
                    date = listOf(),
                ),
            )
        Mockito.`when`(classRepository.findByRegistrationStatusAndIsPublishedTrue(false)).thenReturn(classrooms)

        val result = classService.findAllClass(false)
        assertEquals(classrooms, result)
    }

    @Test
    fun `should return all classes is not published and registrationStatus is false`() {
        val classrooms =
            listOf(
                Classroom(
                    title = "Spring Boot 101",
                    details = "Learn how to build web apps with Spring Boot",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    isPublished = false,
                    registrationStatus = false,
                    date = listOf(),
                ),
            )
        Mockito.`when`(classRepository.findByRegistrationStatusAndIsPublishedTrue(false)).thenReturn(classrooms)

        val result = classService.findAllClass(false)
        assertEquals(classrooms, result)
    }

    @Test
    fun `should return all classes is not published and registrationStatus is true`() {
        val classrooms =
            listOf(
                Classroom(
                    title = "Spring Boot 101",
                    details = "Learn how to build web apps with Spring Boot",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    isPublished = false,
                    registrationStatus = true,
                    date = listOf(),
                ),
            )
        Mockito.`when`(classRepository.findByRegistrationStatusAndIsPublishedTrue(true)).thenReturn(classrooms)

        val result = classService.findAllClass(true)
        assertEquals(classrooms, result)
    }

    @Test
    fun `should return class by id`() {
        val classId = "1"
        val classroomObj =
            Classroom(
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
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))

        val result = classService.findClassById(classId)
        assertEquals(classroomObj, result)
    }

    @Test
    fun `should insert a new class`() {
        val classroomObj =
            Classroom(
                title = "React Native",
                details = "Learn how to build mobile apps with React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                date = listOf(),
            )
        Mockito.`when`(classRepository.insert(classroomObj)).thenReturn(classroomObj)

        val result = classService.insertClass(classroomObj)
        assertEquals(classroomObj, result)
    }

    @Test
    fun `should update venue of a class`() {
        val classId = "1"
        val venues =
            Venue(
                id = "1",
                name = "TRAIN_3",
            )
        val classroomObj =
            Classroom(
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
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)

        val result = classService.updateVenueClass(classId, "1")
        assertEquals(classroomObj, result)
    }

    @Test
    fun `should update meeting url of a class`() {
        val classId = "1"
        val meetingUrl = "https://meet.google.com/abc-xyz"
        val classroomObj =
            Classroom(
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
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)

        val result = classService.updateMeetingUrlClass(classId, meetingUrl)
        assertEquals(classroomObj, result)
    }

    @Test
    fun `should update content of a class`() {
        val classId = "1"
        val classContent = "{\"key\": \"value\"}"
        val classroomObj =
            Classroom(
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
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)

        val result = classService.updateContent(classId, classContent)
        assertEquals(classroomObj, result)
    }

    @Test
    fun `should update registration url of a class`() {
        val classId = "1"
        val registrationUrl = "https://example.com/register"
        val classroomObj =
            Classroom(
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
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)

        val result = classService.updateRegistrationUrl(classId, registrationUrl)
        assertEquals(classroomObj, result)
    }

    @Test
    fun `should toggle registration status of a class`() {
        val classId = "1"
        val classroomObj =
            Classroom(
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
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)

        val result = classService.toggleRegistrationStatus(classId)
        assertEquals(classroomObj, result)
    }

    @Test
    fun `should toggle publish status of a class`() {
        val classId = "1"
        val classroomObj =
            Classroom(
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
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)

        val result = classService.togglePublishStatus(classId)
        assertEquals(classroomObj, result)
    }
}
