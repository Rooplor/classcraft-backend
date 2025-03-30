package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.configs.TestConfig
import com.rooplor.classcraftbackend.configs.TestSecurityConfig
import com.rooplor.classcraftbackend.dtos.InitClassDTO
import com.rooplor.classcraftbackend.entities.Classroom
import com.rooplor.classcraftbackend.enums.ClassType
import com.rooplor.classcraftbackend.enums.Format
import com.rooplor.classcraftbackend.enums.VenueStatus
import com.rooplor.classcraftbackend.repositories.ClassroomRepository
import com.rooplor.classcraftbackend.services.AuthService
import com.rooplor.classcraftbackend.services.ClassService
import com.rooplor.classcraftbackend.services.FormSubmissionService
import com.rooplor.classcraftbackend.types.DateDetail
import com.rooplor.classcraftbackend.types.DateWithVenue
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.anyString
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebMvcTest(ClassController::class)
@Import(TestSecurityConfig::class, TestConfig::class)
@ActiveProfiles("test")
class ClassroomControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var classService: ClassService

    @MockBean
    private lateinit var classRepository: ClassroomRepository

    @MockBean
    private lateinit var formSubmissionService: FormSubmissionService

    @MockBean
    private lateinit var authService: AuthService

    @MockBean
    private lateinit var modelMapper: ModelMapper

    @Test
    fun `should return all classes with registration status`() {
        val classrooms =
            listOf(
                Classroom(
                    title = "React Native",
                    details = "Learn how to build mobile apps using React Native",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    dates = listOf(),
                ),
                Classroom(
                    title = "Spring Boot 101",
                    details = "Learn how to build web apps using Spring Boot",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    dates = listOf(),
                ),
            )
        Mockito.`when`(authService.getUserId()).thenReturn("1")
        Mockito.`when`(formSubmissionService.getFormSubmissionsByClassroomId(anyString())).thenReturn(emptyList())
        Mockito.`when`(classService.findAllClassPublishedWithRegistrationCondition(true)).thenReturn(classrooms)

        mockMvc
            .perform(
                get("/api/class")
                    .param("registrationStatus", "true"),
            )
        Mockito.verify(classService).findAllClassPublishedWithRegistrationCondition(true)
    }

    @Test
    fun `should return all classes with no condition`() {
        val classrooms =
            listOf(
                Classroom(
                    title = "React Native",
                    details = "Learn how to build mobile apps using React Native",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    dates = listOf(),
                ),
                Classroom(
                    title = "Spring Boot 101",
                    details = "Learn how to build web apps using Spring Boot",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    dates = listOf(),
                ),
            )
        Mockito.`when`(formSubmissionService.getFormSubmissionsByClassroomId(anyString())).thenReturn(emptyList())
        Mockito.`when`(authService.getUserId()).thenReturn("1")
        Mockito.`when`(classService.findAllClassPublished()).thenReturn(classrooms)

        mockMvc
            .perform(get("/api/class"))

        Mockito.verify(classService).findAllClassPublished()
    }

    @Test
    fun `should return classes by owners`() {
        val owners = listOf("owner1", "owner2")
        val classrooms =
            listOf(
                Classroom(
                    title = "React Native",
                    details = "Learn how to build mobile apps using React Native",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    dates = listOf(),
                ),
                Classroom(
                    title = "Spring Boot 101",
                    details = "Learn how to build web apps using Spring Boot",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    dates = listOf(),
                ),
            )

        Mockito.`when`(formSubmissionService.getFormSubmissionsByClassroomId(anyString())).thenReturn(emptyList())
        Mockito.`when`(authService.getUserId()).thenReturn("1")
        Mockito.`when`(classService.findClassByOwners(owners)).thenReturn(classrooms)

        mockMvc
            .perform(get("/api/class?userId=owner1,owner2").param("userId", "owner1,owner2"))
            .andExpect(status().isOk)
    }

    @Test
    fun `should return class by id`() {
        val classId = "1"
        val classroomObj =
            Classroom(
                id = classId,
                title = "React Native",
                details = "Learn how to build mobile apps using React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                dates = listOf(),
            )
        Mockito.`when`(classService.findClassById(classId)).thenReturn(classroomObj)

        mockMvc
            .perform(get("/api/class/$classId"))
            .andExpect(status().isOk)
    }

    @Test
    fun `should insert a new class`() {
        val classroomObj =
            Classroom(
                title = "React Native",
                details = "Learn how to build mobile apps using React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                instructorName = "John Doe",
                instructorBio = "John Doe is a software engineer",
                instructorAvatar = "https://example.com/johndoe.jpg",
                instructorFamiliarity = "John Doe has 5 years of experience",
                coverImage = "https://example.com/cover.jpg",
                dates = listOf(),
                owner = "owner1",
            )
        val initClassDTO =
            InitClassDTO(
                title = "React Native",
                details = "Learn how to build mobile apps using React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                instructorName = "John Doe",
                instructorBio = "John Doe is a software engineer",
                instructorAvatar = "https://example.com/johndoe.jpg",
                instructorFamiliarity = "John Doe has 5 years of experience",
                coverImage = "https://example.com/cover.jpg",
                dates = listOf(),
            )
        Mockito.`when`(modelMapper.map(initClassDTO, Classroom::class.java)).thenReturn(classroomObj)
        Mockito.`when`(classService.insertClass(classroomObj)).thenReturn(classroomObj)

        mockMvc
            .perform(
                post("/api/class")
                    .contentType("application/json")
                    .content(
                        """
                        {
                            "title": "React Native",
                            "details": "Learn how to build mobile apps using React Native",
                            "target": "Beginner",
                            "prerequisite": "None",
                            "type": "LECTURE",
                            "format": "ONSITE",
                            "capacity": 30,
                            "instructorName": "John Doe",
                            "instructorBio": "John Doe is a software engineer",
                            "instructorAvatar": "https://example.com/johndoe.jpg",
                            "instructorFamiliarity": "John Doe has 5 years of experience",
                            "coverImage": "https://example.com/cover.jpg",
                            "classMaterials": [],
                            "dates": [],
                            "owner": "owner1",
                            "coOwners": []
                        }
                        """.trimIndent(),
                    ),
            ).andExpect(status().isCreated)
    }

    @Test
    fun `should update venue of a class`() {
        val classId = "1"
        val classroomObj =
            Classroom(
                id = classId,
                title = "React Native",
                details = "Learn how to build mobile apps using React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                dates = listOf(),
                venueStatus = VenueStatus.PENDING.id,
            )
        val classroomResult =
            Classroom(
                id = classId,
                title = "React Native",
                details = "Learn how to build mobile apps using React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                dates = listOf(DateWithVenue(date = DateDetail(), listOf("1"))),
                venueStatus = VenueStatus.PENDING.id,
            )
        Mockito
            .`when`(
                classService.updateDateWithVenueClass(classId, listOf(DateWithVenue(date = DateDetail(), listOf("1")))),
            ).thenReturn(classroomResult)

        mockMvc
            .perform(
                patch("/api/class/$classId/venue")
                    .contentType("application/json")
                    .content(
                        """
                        [{
                            "date": {
                                "startDateTime": "2024-11-19T08:00:00.000",
                                "endDateTime": "2024-11-19T16:00:00.000"
                            },
                            "venueId": ["1"]
                        }]
                        """.trimIndent(),
                    ),
            ).andExpect(status().isOk)
    }

    @Test
    fun `should update meeting url of a class`() {
        val classId = "1"
        val meetingUrl = "https://meet.google.com/abc-xyz"
        val classroomObj =
            Classroom(
                id = classId,
                title = "React Native",
                details = "Learn how to build mobile apps using React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                dates = listOf(),
            )
        Mockito.`when`(classService.updateMeetingUrlClass(classId, meetingUrl)).thenReturn(classroomObj)

        mockMvc
            .perform(
                patch("/api/class/$classId/meeting-url")
                    .contentType("application/json")
                    .content(
                        """
                        "https://meet.google.com/abc-xyz"
                        """.trimIndent(),
                    ),
            ).andExpect(status().isOk)
    }

    @Test
    fun `should update content of a class`() {
        val classId = "1"
        val content =
            """
            {
                "title": "React Native",
                "details": "Learn how to build mobile apps using React Native",
                "target": "Beginner",
                "prerequisite": "None",
                "type": "LECTURE",
                "format": "ONSITE",
                "capacity": 30,
                "dates": []
            }
            """.trimIndent()
        val classroomObj =
            Classroom(
                id = classId,
                title = "React Native",
                details = "Learn how to build mobile apps using React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                dates = listOf(),
            )
        Mockito.`when`(classService.updateContent(classId, content)).thenReturn(classroomObj)

        mockMvc
            .perform(
                patch("/api/class/$classId/content")
                    .contentType("application/json")
                    .content(content),
            ).andExpect(status().isOk)
    }

    @Test
    fun `should update registration url of a class`() {
        val classId = "1"
        val registrationUrl = "https://example.com/register"
        val classroomObj =
            Classroom(
                id = classId,
                title = "React Native",
                details = "Learn how to build mobile apps using React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                dates = listOf(),
            )
        Mockito.`when`(classService.updateRegistrationUrl(classId, registrationUrl)).thenReturn(classroomObj)

        mockMvc
            .perform(
                patch("/api/class/$classId/registration-url")
                    .contentType("application/json")
                    .content(
                        """
                        "https://example.com/register"
                        """.trimIndent(),
                    ),
            ).andExpect(status().isOk)
    }

    @Test
    fun `should toggle registration status of a class`() {
        val classId = "1"
        val classroomObj =
            Classroom(
                id = classId,
                title = "React Native",
                details = "Learn how to build mobile apps using React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                dates = listOf(),
            )
        Mockito.`when`(classService.setRegistrationStatus(classId, true)).thenReturn(classroomObj)

        mockMvc
            .perform(
                patch("/api/class/$classId/set-registration-status")
                    .contentType("application/json")
                    .content(
                        """
                        {
                            "status": true
                        }
                        """.trimIndent(),
                    ),
            ).andExpect(status().isOk)
    }

    @Test
    fun `should toggle publish status of a class`() {
        val classId = "1"
        val classroomObj =
            Classroom(
                id = classId,
                title = "React Native",
                details = "Learn how to build mobile apps using React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                dates = listOf(),
            )
        Mockito.`when`(classService.setPublishStatus(classId, true)).thenReturn(classroomObj)

        mockMvc
            .perform(
                patch("/api/class/$classId/set-publish-status")
                    .contentType("application/json")
                    .content(
                        """
                        {
                            "status": true
                        }
                        """.trimIndent(),
                    ),
            ).andExpect(status().isOk)
    }

    @Test
    fun `should update class`() {
        val classId = "1"
        val classroomObj =
            Classroom(
                id = classId,
                title = "React Native",
                details = "Learn how to build mobile apps using React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                instructorName = "John Doe",
                instructorBio = "John Doe is a software engineer",
                instructorAvatar = "https://example.com/johndoe.jpg",
                instructorFamiliarity = "John Doe has 5 years of experience",
                coverImage = "https://example.com/cover.jpg",
                dates = listOf(),
                owner = "owner1",
            )
        val initClassDTO =
            InitClassDTO(
                title = "React Native",
                details = "Learn how to build mobile apps using React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                instructorName = "John Doe",
                instructorBio = "John Doe is a software engineer",
                instructorAvatar = "https://example.com/johndoe.jpg",
                instructorFamiliarity = "John Doe has 5 years of experience",
                coverImage = "https://example.com/cover.jpg",
                dates = listOf(),
            )
        Mockito.`when`(modelMapper.map(initClassDTO, Classroom::class.java)).thenReturn(classroomObj)
        Mockito.`when`(classService.updateClass(classId, classroomObj)).thenReturn(classroomObj)

        mockMvc
            .perform(
                put("/api/class/$classId")
                    .contentType("application/json")
                    .content(
                        """
                        {
                            "title": "React Native",
                            "details": "Learn how to build mobile apps using React Native",
                            "target": "Beginner",
                            "prerequisite": "None",
                            "type": "LECTURE",
                            "format": "ONSITE",
                            "capacity": 30,
                            "instructorName": "John Doe",
                            "instructorBio": "John Doe is a software engineer",
                            "instructorAvatar": "https://example.com/johndoe.jpg",
                            "instructorFamiliarity": "John Doe has 5 years of experience",
                            "coverImage": "https://example.com/cover.jpg",
                            "classMaterials": [],
                            "dates": [],
                            "owner": "owner1",
                            "coOwners": []
                        }
                        """.trimIndent(),
                    ),
            ).andExpect(status().isOk)
    }

    @Test
    fun `should update classroom stepper status`() {
        val classId = "1"
        val classroomObj =
            Classroom(
                id = classId,
                title = "React Native",
                details = "Learn how to build mobile apps using React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                dates = listOf(),
            )
        Mockito.`when`(classService.updateStepperStatus(classId, 2)).thenReturn(classroomObj)

        mockMvc
            .perform(patch("/api/class/$classId/stepper-status?status=1"))
            .andExpect(status().isOk)
    }

    @Test
    fun `should return 400 when update classroom stepper status with unsupport status id`() {
        val classId = "1"
        Mockito
            .`when`(classService.updateStepperStatus(classId, 5))
            .thenThrow(IllegalArgumentException("Stepper status is not valid"))

        mockMvc
            .perform(patch("/api/class/$classId/stepper-status?status=5"))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `should reserve venue successfully`() {
        val classId = "1"
        val classroom =
            Classroom(
                id = classId,
                title = "React Native",
                details = "Learn how to build mobile apps using React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                dates = listOf(),
            )
        val dateWithVenue =
            listOf(
                DateWithVenue(
                    date =
                        DateDetail(
                            startDateTime = LocalDateTime.parse("2024-11-19T08:00:00.000"),
                            endDateTime = LocalDateTime.parse("2024-11-19T16:00:00.000"),
                        ),
                    venueId =
                        listOf(
                            "67388208776cc565fae80e51",
                            "6738820d776cc565fae80e52",
                            "67388212776cc565fae80e54",
                        ),
                ),
            )
        val requestJson =
            """
             [{
              "date": {
                "startDateTime": "2024-11-19T08:00:00.000",
                "endDateTime": "2024-11-19T16:00:00.000"
              },
              "venueId": [
                "67388208776cc565fae80e51", "6738820d776cc565fae80e52", "67388212776cc565fae80e54"
              ]
            },
            {
              "date": {
                "startDateTime": "2024-11-20T08:00:00.000",
                "endDateTime": "2024-11-20T16:00:00.000"
              },
              "venueId": [
                "67388212776cc565fae80e54", "67388214776cc565fae80e55"
              ]
            }]
            """.trimIndent()

        `when`(classService.findClassById(classId)).thenReturn(classroom)
        doNothing().`when`(classService).reservationVenue(classroom, dateWithVenue, "reservation", "staff@mail.com")

        // Act & Assert
        mockMvc
            .perform(
                post("/api/class/$classId/reservation")
                    .contentType("application/json")
                    .content(requestJson),
            ).andExpect(status().isOk)
    }

    @Test
    fun `should return class when search`() {
        val classrooms =
            listOf(
                Classroom(
                    title = "React Native",
                    details = "Learn how to build mobile apps using React Native",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    dates = listOf(),
                    isPublished = true,
                ),
                Classroom(
                    title = "Spring Boot 101",
                    details = "Learn how to build web apps using Spring Boot",
                    target = "Beginner",
                    prerequisite = "None",
                    type = ClassType.LECTURE,
                    format = Format.ONSITE,
                    capacity = 30,
                    dates = listOf(),
                    isPublished = true,
                ),
            )

        Mockito.`when`(classService.searchClassByTitleOrDetails("React Native")).thenReturn(classrooms)

        mockMvc
            .perform(get("/api/class/search?keyword=React Native"))
            .andExpect(status().isOk)
    }

    @Test
    fun `should update venue status to approved`() {
        val classId = "1"
        val classroomObj =
            Classroom(
                id = classId,
                title = "React Native",
                details = "Learn how to build mobile apps using React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                dates = listOf(),
                venueStatus = VenueStatus.PENDING.id,
            )
        val rejectReason = "Venue is not available"
        Mockito
            .`when`(classService.updateVenueStatus(classId, VenueStatus.REJECTED.id, rejectReason))
            .thenReturn(classroomObj)

        mockMvc
            .perform(
                get("/api/class/$classId/venue-status?venueStatusId=2"),
            ).andExpect(status().isOk)
    }

    @Test
    fun `should update venue status to pending`() {
        val classId = "1"
        val classroomObj =
            Classroom(
                id = classId,
                title = "React Native",
                details = "Learn how to build mobile apps using React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                dates = listOf(),
                venueStatus = VenueStatus.PENDING.id,
            )
        val rejectReason = "Venue is not available"
        Mockito
            .`when`(classService.updateVenueStatus(classId, VenueStatus.REJECTED.id, rejectReason))
            .thenReturn(classroomObj)

        mockMvc
            .perform(
                get("/api/class/$classId/venue-status?venueStatusId=3"),
            ).andExpect(status().isOk)
    }

    @Test
    fun `should update venue status with reject reason`() {
        val classId = "1"
        val classroomObj =
            Classroom(
                id = classId,
                title = "React Native",
                details = "Learn how to build mobile apps using React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                dates = listOf(),
                venueStatus = VenueStatus.PENDING.id,
            )
        val rejectReason = "Venue is not available"
        Mockito
            .`when`(classService.updateVenueStatus(classId, VenueStatus.REJECTED.id, rejectReason))
            .thenReturn(classroomObj)

        mockMvc
            .perform(
                get("/api/class/$classId/venue-status?venueStatusId=4&rejectReason=Venue is not available"),
            ).andExpect(status().isOk)
    }

    @Test
    fun `should return 400 when update venue status without reject reason`() {
        val classId = "1"
        val classroomObj =
            Classroom(
                id = classId,
                title = "React Native",
                details = "Learn how to build mobile apps using React Native",
                target = "Beginner",
                prerequisite = "None",
                type = ClassType.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                dates = listOf(),
                venueStatus = VenueStatus.PENDING.id,
                rejectReason = "",
            )
        val rejectReason = ""
        Mockito
            .`when`(
                classService.updateVenueStatus(classId, VenueStatus.REJECTED.id, rejectReason),
            ).thenThrow(IllegalArgumentException("Reject reason is required"))

        mockMvc
            .perform(
                get("/api/class/$classId/venue-status?venueStatusId=4"),
            ).andExpect(status().isBadRequest)
    }

    @Test
    fun `should update class materials successfully`() {
        val classId = "1"
        val materials = listOf("Material1", "Material2")
        val classroom = Classroom(id = classId, classMaterials = materials)

        `when`(classService.updateClassMaterials(classId, materials)).thenReturn(classroom)

        mockMvc
            .perform(
                patch("/api/class/$classId/materials")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""["Material1", "Material2"]"""),
            ).andExpect(status().isOk)
            .andExpect(content().json("""{"success":true,"result":{"id":"1","classMaterials":["Material1","Material2"]},"error":null}"""))

        verify(classService).updateClassMaterials(classId, materials)
    }

    @Test
    fun `should return bad request when update class materials fails`() {
        val classId = "1"
        val materials = listOf("Material1", "Material2")
        val errorMessage = "Failed to update materials"

        `when`(classService.updateClassMaterials(classId, materials)).thenThrow(RuntimeException(errorMessage))

        mockMvc
            .perform(
                patch("/api/class/$classId/materials")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""["Material1", "Material2"]"""),
            ).andExpect(status().isBadRequest)
            .andExpect(content().json("""{"success":false,"result":null,"error":"$errorMessage"}"""))

        verify(classService).updateClassMaterials(classId, materials)
    }

    @Test
    fun `should return bad request when materials list is empty`() {
        val classId = "1"
        val materials = emptyList<String>()
        val errorMessage = "Materials list cannot be empty"

        `when`(classService.updateClassMaterials(classId, materials)).thenThrow(IllegalArgumentException(errorMessage))

        mockMvc
            .perform(
                patch("/api/class/$classId/materials")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""[]"""),
            ).andExpect(status().isBadRequest)
            .andExpect(content().json("""{"success":false,"result":null,"error":"$errorMessage"}"""))

        verify(classService).updateClassMaterials(classId, materials)
    }
}
