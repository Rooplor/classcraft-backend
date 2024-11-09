package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.Classroom
import com.rooplor.classcraftbackend.entities.User
import com.rooplor.classcraftbackend.entities.Venue
import com.rooplor.classcraftbackend.enums.ClassType
import com.rooplor.classcraftbackend.enums.Format
import com.rooplor.classcraftbackend.repositories.ClassroomRepository
import com.rooplor.classcraftbackend.services.mail.MailService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import org.thymeleaf.context.Context
import java.time.LocalDateTime
import java.util.Optional
import kotlin.test.assertFailsWith

@SpringBootTest
class ClassroomServiceTest {
    private val classRepository: ClassroomRepository = Mockito.mock(ClassroomRepository::class.java)
    private val venueService: VenueService = Mockito.mock(VenueService::class.java)
    private val authService: AuthService = Mockito.mock(AuthService::class.java)
    private val userService: UserService = Mockito.mock(UserService::class.java)
    private val mailService: MailService = Mockito.mock(MailService::class.java)
    private val classService: ClassService = ClassService(classRepository, venueService, authService, userService, mailService)

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
        Mockito
            .`when`(classRepository.findByRegistrationStatusAndIsPublishedTrueOrderByCreatedWhen(true))
            .thenReturn(classrooms)

        val result = classService.findAllClassPublishedWithRegistrationCondition(true)
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
        Mockito
            .`when`(classRepository.findByRegistrationStatusAndIsPublishedTrueOrderByCreatedWhen(false))
            .thenReturn(classrooms)

        val result = classService.findAllClassPublishedWithRegistrationCondition(false)
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
        Mockito
            .`when`(classRepository.findByRegistrationStatusAndIsPublishedTrueOrderByCreatedWhen(false))
            .thenReturn(classrooms)

        val result = classService.findAllClassPublishedWithRegistrationCondition(false)
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
        Mockito
            .`when`(classRepository.findByRegistrationStatusAndIsPublishedTrueOrderByCreatedWhen(true))
            .thenReturn(classrooms)

        val result = classService.findAllClassPublishedWithRegistrationCondition(true)
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
                owner = "owner1",
            )
        Mockito.`when`(classRepository.insert(classroomObj)).thenReturn(classroomObj)
        Mockito
            .`when`(
                authService.getAuthenticatedUserDetails(),
            ).thenReturn(User(id = "owner1", username = "owner1", email = "owner1@mail.com", profilePicture = null))

        val result = classService.insertClass(classroomObj)
        assertEquals(classroomObj, result)
    }

    @Test
    fun `should update venue of a class`() {
        val classId = "1"
        val venues =
            listOf(
                Venue(
                    id = "1",
                    name = "TRAIN_3",
                ),
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
                venueStatus = 1,
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)

        val result = classService.updateVenueClass(classId, listOf("1", "2"))
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

    @Test
    fun `should update class by id`() {
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
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)

        val result = classService.updateClass(classId, classroomObj)
        assertEquals(classroomObj, result)
    }

    @Test
    fun `should return classes by owners`() {
        val owners = listOf("owner1", "owner2")
        val classroomsByOwner1 =
            listOf(
                Classroom(
                    id = "1",
                    title = "React Native",
                    details = "Learn how to build mobile apps using React Native",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    date = listOf(),
                    owner = "owner1",
                ),
                Classroom(
                    id = "2",
                    title = "Spring Boot 101",
                    details = "Learn how to build web apps using Spring Boot",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    date = listOf(),
                    owner = "owner1",
                ),
            )
        val classroomsByOwner2 =
            listOf(
                Classroom(
                    id = "3",
                    title = "React Native - Advanced",
                    details = "Learn how to build mobile apps using React Native",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    date = listOf(),
                    owner = "owner1",
                ),
                Classroom(
                    id = "4",
                    title = "Spring Boot 101 - Advanced",
                    details = "Learn how to build web apps using Spring Boot",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    date = listOf(),
                    owner = "owner1",
                ),
            )

        val classrooms = classroomsByOwner1 + classroomsByOwner2
        Mockito.`when`(classRepository.findByOwner("owner1")).thenReturn(classroomsByOwner1)
        Mockito.`when`(classRepository.findByOwner("owner2")).thenReturn(classroomsByOwner2)

        val result = classService.findClassByOwners(owners)
        assertEquals(classrooms, result)
    }

    @Test
    fun `should update stepper status of a class`() {
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
                stepperStatus = 1,
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)

        val fillCraftDetail = classService.updateStepperStatus(classId, 2)
        assertEquals(fillCraftDetail.stepperStatus, 2)
        val reserveVenue = classService.updateStepperStatus(classId, 3)
        assertEquals(reserveVenue.stepperStatus, 3)
        val craftContent = classService.updateStepperStatus(classId, 4)
        assertEquals(craftContent.stepperStatus, 4)
    }

    @Test
    fun `should throw error when update stepper status of a class with unsupport id`() {
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
                stepperStatus = 1,
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)

        assertThrows<Exception> {
            classService.updateStepperStatus(classId, 5)
        }
    }

    @Test
    fun `should get all class without condition and published`() {
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
                    date = listOf(),
                ),
            )
        Mockito
            .`when`(classRepository.findByIsPublishedTrueOrderByCreatedWhen())
            .thenReturn(classrooms)

        val result = classService.findAllClassPublished()
        assertEquals(classrooms, result)
    }

    @Test
    fun `should send reservation email successfully`() {
        // Arrange
        val venueIds = listOf("1", "2")
        val staffUsername = "testUser"

        val classroom =
            Classroom(
                owner = "1",
                title = "React Native",
                details = "Learn how to build mobile apps with React Native",
                date = listOf(LocalDateTime.of(2024, 10, 1, 10, 0), LocalDateTime.of(2024, 10, 2, 10, 0)),
            )
        val user = User(username = "testUser", profilePicture = "profile.jpg")
        val venue = Venue(name = "LX10 - 4")

        `when`(authService.getAuthenticatedUser()).thenReturn(staffUsername)
        `when`(userService.findByUsername(staffUsername)).thenReturn(user)
        `when`(userService.findUserById(classroom.owner)).thenReturn(User(username = "ownerUsername"))
        `when`(venueService.findVenueById("1")).thenReturn(venue)
        `when`(venueService.findVenueById("2")).thenReturn(venue)

        // Act
        classService.reservationVenue(classroom, venueIds)

        // Assert
        verify(authService).getAuthenticatedUser()
        verify(userService).findByUsername(staffUsername)
        verify(userService).findUserById(classroom.owner)
        verify(venueService).findVenueById("1")
        verify(venueService).findVenueById("2")
        verify(mailService).sendEmail(
            subject = eq("[ClassCraft] Reservation venue for ${classroom.title} request from ${user.username}"),
            template = eq("reservation"),
            context = any(Context::class.java),
        )
    }

    @Test
    fun `should throw exception when user is not authenticated`() {
        `when`(authService.getAuthenticatedUser()).thenReturn(null)

        val classroom =
            Classroom(
                owner = "1",
                title = "React Native",
                details = "Learn how to build mobile apps with React Native",
                date = listOf(LocalDateTime.of(2024, 10, 1, 10, 0), LocalDateTime.of(2024, 10, 2, 10, 0)),
            )

        assertFailsWith<Exception> {
            classService.reservationVenue(classroom, listOf("1", "2"))
        }
    }

    @Test
    fun `should update venue status of a class`() {
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
                venueStatus = 1,
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)

        val fillCraftDetail = classService.updateVenueStatus(classId, 2)
        assertEquals(fillCraftDetail.venueStatus, 2)
        val reserveVenue = classService.updateVenueStatus(classId, 3)
        assertEquals(reserveVenue.venueStatus, 3)
    }

    @Test
    fun `should throw error when update venue status of a class with unsupport id`() {
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
                venueStatus = 1,
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)

        assertThrows<Exception> {
            classService.updateVenueStatus(classId, 4)
        }
    }
}
