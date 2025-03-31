package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.Classroom
import com.rooplor.classcraftbackend.entities.Form
import com.rooplor.classcraftbackend.entities.User
import com.rooplor.classcraftbackend.entities.Venue
import com.rooplor.classcraftbackend.enums.ClassType
import com.rooplor.classcraftbackend.enums.Format
import com.rooplor.classcraftbackend.enums.VenueStatus
import com.rooplor.classcraftbackend.helpers.ClassroomHelper
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.ClassroomRepository
import com.rooplor.classcraftbackend.repositories.FormSubmissionRepository
import com.rooplor.classcraftbackend.repositories.ReopenRequestRepository
import com.rooplor.classcraftbackend.services.mail.MailService
import com.rooplor.classcraftbackend.types.DateDetail
import com.rooplor.classcraftbackend.types.DateWithVenue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.context.SpringBootTest
import org.thymeleaf.context.Context
import java.time.LocalDateTime
import java.util.Optional
import kotlin.test.assertFailsWith

@SpringBootTest
class ClassroomServiceTest {
    private val classRepository: ClassroomRepository = Mockito.mock(ClassroomRepository::class.java)
    private val formSubmissionRepository: FormSubmissionRepository = Mockito.mock(FormSubmissionRepository::class.java)
    private val reopenRequestRepository: ReopenRequestRepository = Mockito.mock(ReopenRequestRepository::class.java)
    private val venueService: VenueService = Mockito.mock(VenueService::class.java)
    private val authService: AuthService = Mockito.mock(AuthService::class.java)
    private val userService: UserService = Mockito.mock(UserService::class.java)
    private val mailService: MailService = Mockito.mock(MailService::class.java)
    private var classService: ClassService = Mockito.mock(ClassService::class.java)
    private val formService: FormService = Mockito.mock(FormService::class.java)
    private val classroomHelper: ClassroomHelper = Mockito.mock(ClassroomHelper::class.java)

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        classService =
            ClassService(
                classRepository,
                formSubmissionRepository,
                reopenRequestRepository,
                venueService,
                authService,
                userService,
                mailService,
                formService,
                classroomHelper,
            )
    }

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
                    dates = listOf(),
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
                    dates = listOf(),
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
                    dates = listOf(),
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
                    dates = listOf(),
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
                    dates = listOf(),
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
                    dates = listOf(),
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
                dates = listOf(),
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
                dates = listOf(),
                owner = "owner1",
            )
        val form =
            Form(
                classroomId = "1",
                title = "Form title",
                description = "Form description",
                openDate = LocalDateTime.now(),
                closeDate = LocalDateTime.now(),
                fields = listOf(),
                isOwnerApprovalRequired = false,
            )
        val classroomResult = classroomObj.copy(id = "1")
        Mockito.`when`(classRepository.insert(classroomObj)).thenReturn(classroomResult)
        Mockito
            .`when`(
                authService.getUserId(),
            ).thenReturn("owner1")
        Mockito.`when`(formService.createForm(form)).thenReturn(form)

        val result = classService.insertClass(classroomObj)
        assertEquals(classroomResult, result)
    }

    @Test
    fun `should update venue of a class`() {
        val classId = "1"
        val venues =
            listOf(
                Venue(
                    id = "1",
                    room = "TRAIN_3",
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
                dates = listOf(),
                venueStatus = 1,
                owner = "1",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn("admin")
        Mockito.`when`(userService.findByUsername("admin")).thenReturn(User(id = "1", username = "admin"))

        val result = classService.updateDateWithVenueClass(classId, listOf(DateWithVenue(DateDetail(), listOf("1"))))
        assertEquals(classroomObj, result)
    }

    @Test
    fun `should not update venue of a class with owner id doesn't match`() {
        val classId = "1"
        val venues =
            listOf(
                Venue(
                    id = "1",
                    room = "TRAIN_3",
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
                dates = listOf(),
                venueStatus = 1,
                owner = "1",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn("admin")
        Mockito.`when`(userService.findByUsername("user2")).thenReturn(User(id = "2", username = "user2"))

        assertThrows<Exception> { classService.updateDateWithVenueClass(classId, listOf(DateWithVenue(DateDetail(), listOf("1")))) }
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
                dates = listOf(),
                meetingUrl = "https://meet.google.com/abc-xyz",
                owner = "1",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn("admin")
        Mockito.`when`(userService.findByUsername("admin")).thenReturn(User(id = "1", username = "admin"))

        val result = classService.updateMeetingUrlClass(classId, meetingUrl)
        assertEquals(classroomObj, result)
    }

    @Test
    fun `should not update meeting url of a class with owner id doesn't match`() {
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
                dates = listOf(),
                meetingUrl = "https://meet.google.com/abc-xyz",
                owner = "1",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn("admin")
        Mockito.`when`(userService.findByUsername("user2")).thenReturn(User(id = "2", username = "user2"))

        assertThrows<Exception> { classService.updateMeetingUrlClass(classId, meetingUrl) }
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
                dates = listOf(),
                content = "{\"key\": \"value\"}",
                owner = "1",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn("admin")
        Mockito.`when`(userService.findByUsername("admin")).thenReturn(User(id = "1", username = "admin"))

        val result = classService.updateContent(classId, classContent)
        assertEquals(classroomObj, result)
    }

    @Test
    fun `should not update content of a class with owner id doesn't match`() {
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
                dates = listOf(),
                content = "{\"key\": \"value\"}",
                owner = "1",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn("admin")
        Mockito.`when`(userService.findByUsername("user2")).thenReturn(User(id = "2", username = "user2"))

        assertThrows<Exception> { classService.updateContent(classId, classContent) }
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
                dates = listOf(),
                registrationUrl = "https://example.com/register",
                owner = "1",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn("admin")
        Mockito.`when`(userService.findByUsername("admin")).thenReturn(User(id = "1", username = "admin"))

        val result = classService.updateRegistrationUrl(classId, registrationUrl)
        assertEquals(classroomObj, result)
    }

    @Test
    fun `should not update registration url of a class with owner id doesn't match`() {
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
                dates = listOf(),
                registrationUrl = "https://example.com/register",
                owner = "1",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn("admin")
        Mockito.`when`(userService.findByUsername("user2")).thenReturn(User(id = "2", username = "user2"))

        assertThrows<Exception> { classService.updateRegistrationUrl(classId, registrationUrl) }
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
                dates = listOf(),
                registrationStatus = false,
                owner = "1",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn("admin")
        Mockito.`when`(userService.findByUsername("admin")).thenReturn(User(id = "1", username = "admin"))

        val result = classService.setRegistrationStatus(classId, false)
        assertEquals(classroomObj, result)
    }

    @Test
    fun `should not toggle registration status of a class with owner id doesn't match`() {
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
                dates = listOf(),
                registrationStatus = false,
                owner = "1",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn("admin")
        Mockito.`when`(userService.findByUsername("user2")).thenReturn(User(id = "2", username = "user2"))

        assertThrows<Exception> { classService.setRegistrationStatus(classId, false) }
    }

    @Test
    fun `should set publish status of a class`() {
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
                dates = listOf(),
                isPublished = false,
                owner = "1",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn("admin")
        Mockito.`when`(userService.findByUsername("admin")).thenReturn(User(id = "1", username = "admin"))

        val result = classService.setPublishStatus(classId, false)
        assertEquals(classroomObj, result)
    }

    @Test
    fun `should not set publish status of a class with owner id doesn't match`() {
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
                dates = listOf(),
                isPublished = false,
                owner = "1",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn("admin")
        Mockito.`when`(userService.findByUsername("user2")).thenReturn(User(id = "2", username = "user2"))

        assertThrows<Exception> { classService.setPublishStatus(classId, false) }
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
                dates = listOf(),
                owner = "1",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn("admin")
        Mockito.`when`(userService.findByUsername("admin")).thenReturn(User(id = "1", username = "admin"))

        val result = classService.updateClass(classId, classroomObj)
        assertEquals(classroomObj, result)
    }

    @Test
    fun `should not update class by id with owner id doesn't match`() {
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
                dates = listOf(),
                owner = "1",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn("admin")
        Mockito.`when`(userService.findByUsername("user2")).thenReturn(User(id = "2", username = "user2"))

        assertThrows<Exception> { classService.updateClass(classId, classroomObj) }
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
                    dates = listOf(),
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
                    dates = listOf(),
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
                    dates = listOf(),
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
                    dates = listOf(),
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
                dates = listOf(),
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
                dates = listOf(),
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
                    dates = listOf(),
                ),
                Classroom(
                    title = "Spring Boot 101",
                    details = "Learn how to build web apps with Spring Boot",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    dates = listOf(),
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
        val dateWithVenue =
            listOf(
                DateWithVenue(
                    date =
                        DateDetail(
                            startDateTime = LocalDateTime.parse("2024-11-19T08:00:00.000"),
                            endDateTime = LocalDateTime.parse("2024-11-19T16:00:00.000"),
                        ),
                    venueId = listOf("1", "2"),
                ),
            )

        val staffUsername = "admin"

        val classroom =
            Classroom(
                id = "1",
                owner = "1",
                title = "React Native",
                details = "Learn how to build mobile apps with React Native",
            )
        val user = User(id = "1", username = "admin", profilePicture = "profile.jpg")
        val venue1 = Venue(room = "LX10 - 4")
        val venue2 = Venue(room = "LX10 - 5")

        `when`(authService.getAuthenticatedUser()).thenReturn(staffUsername)
        `when`(userService.findByUsername(staffUsername)).thenReturn(user)
        `when`(userService.findUserById(classroom.owner)).thenReturn(User(username = "ownerUsername"))
        `when`(venueService.findVenueById("1")).thenReturn(venue1)
        `when`(venueService.findVenueById("2")).thenReturn(venue2)
        `when`(classRepository.findById("1")).thenReturn(Optional.of(classroom))
        `when`(classRepository.save(classroom)).thenReturn(classroom)
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn(staffUsername)

        classService.reservationVenue(classroom, dateWithVenue)

        val mapDateWithVenueToTemplateMethod = ClassService::class.java.getDeclaredMethod("mapDateWithVenueToTemplate", List::class.java)
        mapDateWithVenueToTemplateMethod.isAccessible = true
        val result = mapDateWithVenueToTemplateMethod.invoke(classService, dateWithVenue)

        verify(mailService).sendEmail(
            subject = eq("[ClassCraft] Reservation venue for ${classroom.title} request from ${user.username}"),
            template = eq("reservation"),
            context = any(Context::class.java),
            to = eq(null),
        )

        val context = Context()
        context.setVariable("dateWithVenue", result)
        val dateWithVenueVariable = context.getVariable("dateWithVenue")
        assertNotNull(dateWithVenueVariable)
        assertEquals(result, dateWithVenueVariable)
    }

    @Test
    fun `should throw exception when user is not authenticated`() {
        `when`(authService.getAuthenticatedUser()).thenReturn(null)

        val classroom =
            Classroom(
                owner = "1",
                title = "React Native",
                details = "Learn how to build mobile apps with React Native",
            )

        assertFailsWith<Exception> {
            classService.reservationVenue(
                classroom,
                listOf(
                    DateWithVenue(
                        date = DateDetail(),
                        venueId = listOf("1"),
                    ),
                ),
            )
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
                dates = listOf(),
                venueStatus = 1,
                owner = "1",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)
        `when`(userService.findUserById("1")).thenReturn(User(id = "1", username = "admin", email = "123@gmail.com"))

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
                dates = listOf(),
                venueStatus = 1,
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)

        assertThrows<Exception> {
            classService.updateVenueStatus(classId, 4)
        }
    }

    @Test
    fun `should return class when search by title and details`() {
        val title = "React Native"
        val detail = "build mobile apps with react native"
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
                    dates = listOf(),
                ),
            )
        Mockito
            .`when`(
                classRepository.findByIsPublishedTrueAndTitleContainingIgnoreCaseOrDetailsContainingIgnoreCaseAndIsPublishedTrue(
                    title,
                    title,
                ),
            ).thenReturn(classrooms)
        Mockito
            .`when`(
                classRepository.findByIsPublishedTrueAndTitleContainingIgnoreCaseOrDetailsContainingIgnoreCaseAndIsPublishedTrue(
                    detail,
                    detail,
                ),
            ).thenReturn(classrooms)

        val result = classService.searchClassByTitleOrDetails(title)
        assertEquals(classrooms, result)

        val result2 = classService.searchClassByTitleOrDetails(detail)
        assertEquals(classrooms, result2)
    }

    @Test
    fun `should update venue status to approved`() {
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
                dates = listOf(),
                venueStatus = 2,
                owner = "1",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)
        `when`(userService.findUserById("1")).thenReturn(User(id = "1", username = "admin", email = "123@gmail.com"))

        val result = classService.updateVenueStatus(classId, 3, "")
        assertEquals(classroomObj, result)
    }

    @Test
    fun `should update venue status to rejected`() {
        val classId = "1"
        val rejectReason = "Venue is not available"
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
                dates = listOf(),
                venueStatus = 2,
                rejectReason = "Venue is not available",
                owner = "1",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)
        `when`(userService.findUserById("1")).thenReturn(User(id = "1", username = "admin", email = "123@gmail.com"))

        val result = classService.updateVenueStatus(classId, 4, rejectReason)
        assertEquals(classroomObj, result)
    }

    @Test
    fun `should throw error when update venue status to rejected without reason`() {
        val classId = "1"
        val rejectReason = ""
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
                dates = listOf(),
                venueStatus = 2,
                rejectReason = "Venue is not available",
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(classroomObj)).thenReturn(classroomObj)

        assertThrows<IllegalArgumentException> { classService.updateVenueStatus(classId, 4, rejectReason) }
    }

    @Test
    fun `should throw error when insert with invalid input`() {
        val classroomObj =
            Classroom(
                title = "",
                details = "",
                target = "",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 0,
                dates = listOf(),
                owner = "owner1",
                instructorAvatar = "",
                instructorFamiliarity = "",
                instructorName = "",
                instructorBio = "",
            )
        val expectationErrorMessage =
            "Title is required, Details is required, " +
                "Target is required, Capacity must be greater than 0, " +
                "Instructor name is required, Instructor bio is required, " +
                "Instructor familiarity is required, Instructor avatar is required"
        Mockito.`when`(classroomHelper.validateClassroom(classroomObj)).thenThrow(IllegalArgumentException(expectationErrorMessage))

        try {
            classService.insertClass(classroomObj)
        } catch (e: IllegalArgumentException) {
            assertEquals(expectationErrorMessage, e.message)
        }
    }

    @Test
    fun `should throw error when update with invalid input`() {
        val classId = "1"
        val classroomObj =
            Classroom(
                id = classId,
                title = "",
                details = "",
                target = "",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 0,
                dates = listOf(),
                owner = "owner1",
                instructorAvatar = "",
                instructorFamiliarity = "",
                instructorName = "",
                instructorBio = "",
            )
        val expectationErrorMessage =
            "Title is required, Details is required, " +
                "Target is required, Capacity must be greater than 0, " +
                "Instructor name is required, Instructor bio is required, " +
                "Instructor familiarity is required, Instructor avatar is required"
        Mockito.`when`(classroomHelper.validateClassroom(classroomObj)).thenThrow(IllegalArgumentException(expectationErrorMessage))

        try {
            classService.updateClass(classId, classroomObj)
        } catch (e: IllegalArgumentException) {
            assertEquals(expectationErrorMessage, e.message)
        }
    }

    @Test
    fun `should delete class and form by id`() {
        val classId = "1"

        Mockito.doNothing().`when`(classRepository).deleteById(classId)
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(Classroom(id = classId, owner = "owner1")))
        Mockito.doNothing().`when`(formService).deleteFormById(classId)

        classService.deleteClass(classId)

        verify(classRepository, times(1)).deleteById(classId)
        verify(formService, times(1)).deleteFormById(classId)
    }

    @Test
    fun `should throw error when class is published and dates are changed`() {
        val classId = "1"
        val originalClassroom =
            Classroom(
                id = classId,
                title = "Original Title",
                details = "Original Details",
                target = "Original Target",
                prerequisite = "Original Prerequisite",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                dates = listOf(DateWithVenue(DateDetail(LocalDateTime.now(), LocalDateTime.now()), listOf("1"))),
                isPublished = true,
                venueStatus = VenueStatus.APPROVED.id,
                owner = "owner1",
            )
        val updatedClassroom =
            originalClassroom.copy(
                dates = listOf(DateWithVenue(DateDetail(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1)), listOf("1"))),
            )

        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(originalClassroom))
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn("admin")
        Mockito.`when`(userService.findByUsername("admin")).thenReturn(User(id = "owner1", username = "admin"))

        val exception =
            assertThrows<Exception> {
                classService.updateClass(classId, updatedClassroom)
            }

        assertEquals(ErrorMessages.CLASS_CANNOT_CHANGE_DATE, exception.message)
    }

    @Test
    fun `should reset venue status and remove form submission when class is not published and dates are changed`() {
        val classId = "1"
        val originalClassroom =
            Classroom(
                id = classId,
                title = "Original Title",
                details = "Original Details",
                target = "Original Target",
                prerequisite = "Original Prerequisite",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                dates = listOf(DateWithVenue(DateDetail(LocalDateTime.now(), LocalDateTime.now()), listOf("1"))),
                isPublished = false,
                venueStatus = VenueStatus.APPROVED.id,
                owner = "owner1",
            )
        val updatedClassroom =
            originalClassroom.copy(
                dates = listOf(DateWithVenue(DateDetail(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1)), listOf("1"))),
            )

        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(originalClassroom))
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn("admin")
        Mockito.`when`(userService.findByUsername("admin")).thenReturn(User(id = "owner1", username = "admin"))
        Mockito.doNothing().`when`(formService).deleteFormSubmissionByFormId(classId)
        Mockito
            .`when`(
                classRepository.save(any(Classroom::class.java)),
            ).thenReturn(updatedClassroom.copy(venueStatus = VenueStatus.NO_REQUEST.id))

        val result = classService.updateClass(classId, updatedClassroom)

        assertEquals(VenueStatus.NO_REQUEST.id, result.venueStatus)
        verify(formService, times(1)).deleteFormSubmissionByFormId(classId)
    }

    @Test
    fun `should update class materials successfully`() {
        val classId = "1"
        val classMaterials = listOf("Material1", "Material2")
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
                dates = listOf(),
                owner = "1",
                classMaterials = listOf(),
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(classRepository.save(any(Classroom::class.java))).thenReturn(classroomObj.copy(classMaterials = classMaterials))
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn("admin")
        Mockito.`when`(userService.findByUsername("admin")).thenReturn(User(id = "1", username = "admin"))

        val result = classService.updateClassMaterials(classId, classMaterials)
        assertEquals(classMaterials, result.classMaterials)
    }

    @Test
    fun `should throw exception when user is not the owner`() {
        val classId = "1"
        val classMaterials = listOf("Material1", "Material2")
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
                dates = listOf(),
                owner = "1",
                classMaterials = listOf(),
            )
        Mockito.`when`(classRepository.findById(classId)).thenReturn(Optional.of(classroomObj))
        Mockito.`when`(authService.getAuthenticatedUser()).thenReturn("admin")
        Mockito.`when`(userService.findByUsername("admin")).thenReturn(User(id = "2", username = "admin"))

        val exception =
            assertThrows<Exception> {
                classService.updateClassMaterials(classId, classMaterials)
            }
        assertEquals(ErrorMessages.FORBIDDEN, exception.message)
    }
}
