package com.rooplor.classcraftbackend.controllers

import com.rooplor.classcraftbackend.dtos.ClassListDTO
import com.rooplor.classcraftbackend.dtos.InitClassDTO
import com.rooplor.classcraftbackend.dtos.VenueUpdateDTO
import com.rooplor.classcraftbackend.entities.Class
import com.rooplor.classcraftbackend.enums.Format
import com.rooplor.classcraftbackend.enums.Type
import com.rooplor.classcraftbackend.enums.Venue
import com.rooplor.classcraftbackend.enums.VenueStatus
import com.rooplor.classcraftbackend.services.ClassService
import com.rooplor.classcraftbackend.utils.ListMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ClassController::class)
class ClassControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var classService: ClassService

    @MockBean
    private lateinit var modelMapper: ModelMapper

    @MockBean
    private lateinit var listMapper: ListMapper

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
        val classList =
            listOf(
                ClassListDTO(
                    id = "1",
                    title = "Test Class",
                ),
                ClassListDTO(
                    id = "2",
                    title = "Test Class 2",
                ),
            )

        Mockito.`when`(classService.findAllClass()).thenReturn(classes)
        Mockito.`when`(listMapper.mapList(classes, ClassListDTO::class.java, modelMapper)).thenReturn(classList)

        mockMvc
            .perform(get("/api/class"))
            .andExpect(status().isOk)
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
        Mockito.`when`(classService.findClassById(classId)).thenReturn(classObj)

        mockMvc
            .perform(get("/api/class/$classId"))
            .andExpect(status().isOk)
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
        val initClassDTO =
            InitClassDTO(
                title = "Test Class",
                details = "Details",
                target = "Target",
                prerequisite = "None",
                type = Type.LECTURE,
                format = Format.ONSITE,
                capacity = 30,
                date = listOf(),
            )
        Mockito.`when`(modelMapper.map(initClassDTO, Class::class.java)).thenReturn(classObj)
        Mockito.`when`(classService.insertClass(classObj)).thenReturn(classObj)

        mockMvc
            .perform(
                post("/api/class")
                    .contentType("application/json")
                    .content(
                        """
                        {
                            "title": "Test Class",
                            "details": "Details",
                            "target": "Target",
                            "prerequisite": "None",
                            "type": "LECTURE",
                            "format": "ONSITE",
                            "capacity": 30,
                            "date": []
                        }
                        """.trimIndent(),
                    ),
            ).andExpect(status().isOk)
    }

    @Test
    fun `should update venue of a class`() {
        val classId = "1"
        val venueUpdateDTO = VenueUpdateDTO(venue = Venue.TRAIN_3, status = VenueStatus.PENDING)
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
        Mockito.`when`(classService.updateVenueClass(classId, venueUpdateDTO)).thenReturn(classObj)

        mockMvc
            .perform(
                patch("/api/class/$classId/venue")
                    .contentType("application/json")
                    .content(
                        """
                        {
                            "venue": "TRAIN_3",
                            "status": "PENDING"
                        }
                        """.trimIndent(),
                    ),
            ).andExpect(status().isOk)
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
            )
        Mockito.`when`(classService.updateMeetingUrlClass(classId, meetingUrl)).thenReturn(classObj)

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
                "title": "Test Class",
                "details": "Details",
                "target": "Target",
                "prerequisite": "None",
                "type": "LECTURE",
                "format": "ONSITE",
                "capacity": 30,
                "date": []
            }
            """.trimIndent()
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
        Mockito.`when`(classService.updateContent(classId, content)).thenReturn(classObj)

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
        Mockito.`when`(classService.updateRegistrationUrl(classId, registrationUrl)).thenReturn(classObj)

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
        Mockito.`when`(classService.toggleRegistrationStatus(classId)).thenReturn(classObj)

        mockMvc
            .perform(patch("/api/class/$classId/toggle-registration-status"))
            .andExpect(status().isOk)
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
            )
        Mockito.`when`(classService.togglePublishStatus(classId)).thenReturn(classObj)

        mockMvc
            .perform(patch("/api/class/$classId/toggle-publish-status"))
            .andExpect(status().isOk)
    }
}
