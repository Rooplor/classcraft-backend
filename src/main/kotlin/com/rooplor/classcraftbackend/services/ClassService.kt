package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.dtos.DateDetail
import com.rooplor.classcraftbackend.dtos.DateWithVenueDTO
import com.rooplor.classcraftbackend.dtos.StartEndDetail
import com.rooplor.classcraftbackend.entities.Classroom
import com.rooplor.classcraftbackend.enums.Status
import com.rooplor.classcraftbackend.enums.VenueStatus
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.ClassroomRepository
import com.rooplor.classcraftbackend.services.mail.MailService
import com.rooplor.classcraftbackend.types.DateWithVenue
import com.rooplor.classcraftbackend.utils.JsonValid.isValidJson
import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AllArgsConstructor
@Service
class ClassService
    @Autowired
    constructor(
        private val classRepository: ClassroomRepository,
        private val venueService: VenueService,
        private val authService: AuthService,
        private val userService: UserService,
        private val mailService: MailService,
    ) {
        @Value("\${staff.username}")
        private val staffUsername: String? = null

        @Value("\${staff.approve_url}")
        private val approveUrl: String? = null

        @Value("\${staff.reject_url}")
        private val rejectUrl: String? = null

        fun findAllClassPublishedWithRegistrationCondition(registrationStatus: Boolean): List<Classroom> =
            classRepository.findByRegistrationStatusAndIsPublishedTrueOrderByCreatedWhen(registrationStatus)

        fun findAllClassPublished(): List<Classroom> = classRepository.findByIsPublishedTrueOrderByCreatedWhen()

        fun insertClass(addedClassroom: Classroom): Classroom {
            addedClassroom.registrationStatus = false
            addedClassroom.isPublished = false
            addedClassroom.owner = authService.getUserId()
            addedClassroom.stepperStatus = 1
            return classRepository.insert(addedClassroom)
        }

        fun updateClass(
            id: String,
            updatedClassroom: Classroom,
        ): Classroom {
            val classToUpdate = findClassById(id)
            classToUpdate.title = updatedClassroom.title
            classToUpdate.details = updatedClassroom.details
            classToUpdate.target = updatedClassroom.target
            classToUpdate.prerequisite = updatedClassroom.prerequisite
            classToUpdate.type = updatedClassroom.type
            classToUpdate.format = updatedClassroom.format
            classToUpdate.capacity = updatedClassroom.capacity
            classToUpdate.dates = updatedClassroom.dates
            classToUpdate.instructorName = updatedClassroom.instructorName
            classToUpdate.instructorBio = updatedClassroom.instructorBio
            classToUpdate.instructorAvatar = updatedClassroom.instructorAvatar
            classToUpdate.instructorFamiliarity = updatedClassroom.instructorFamiliarity
            classToUpdate.coverImage = updatedClassroom.coverImage
            if (updatedClassroom.coOwners != null) {
                validateCoOwners(updatedClassroom.coOwners!!)
                classToUpdate.coOwners = updatedClassroom.coOwners
            }
            classToUpdate.coOwners = updatedClassroom.coOwners
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        fun findClassById(id: String): Classroom = classRepository.findById(id).orElseThrow()

        fun updateDateWithVenueClass(
            id: String,
            dateWithVenue: List<DateWithVenue>,
        ): Classroom {
            val classToUpdate = findClassById(id)
            classToUpdate.dates = dateWithVenue
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        fun updateMeetingUrlClass(
            id: String,
            meetingUrl: String,
        ): Classroom {
            val classToUpdate = findClassById(id)
            classToUpdate.meetingUrl = meetingUrl
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        fun updateContent(
            id: String,
            classContent: String,
        ): Classroom {
            if (!isValidJson(classContent)) {
                throw IllegalArgumentException("Content is not a valid JSON")
            }
            val classToUpdate = findClassById(id)
            classToUpdate.content = classContent
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        fun updateRegistrationUrl(
            id: String,
            registrationUrl: String,
        ): Classroom {
            val classToUpdate = findClassById(id)
            classToUpdate.registrationUrl = registrationUrl
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        fun toggleRegistrationStatus(id: String): Classroom {
            val classToUpdate = findClassById(id)
            classToUpdate.registrationStatus = !classToUpdate.registrationStatus!!
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        fun updateVenueStatus(
            id: String,
            venueStatus: Int,
        ): Classroom {
            val classToUpdate = findClassById(id)
            if (VenueStatus.values().contains(VenueStatus.values().find { it.id == venueStatus })) {
                classToUpdate.venueStatus = VenueStatus.values().find { it.id == venueStatus }?.id
            } else {
                throw IllegalArgumentException("Venue status is not valid")
            }
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        fun togglePublishStatus(id: String): Classroom {
            val classToUpdate = findClassById(id)
            classToUpdate.isPublished = !classToUpdate.isPublished!!
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        fun deleteClass(id: String) {
            classRepository.deleteById(id)
        }

        fun findClassByOwners(owners: List<String>): List<Classroom> = owners.flatMap { owner -> classRepository.findByOwner(owner) }

        fun updateStepperStatus(
            id: String,
            stepperStatus: Int,
        ): Classroom {
            val classToUpdate = findClassById(id)
            if (Status.values().contains(Status.values().find { it.id == stepperStatus })) {
                classToUpdate.stepperStatus = stepperStatus
            } else {
                throw IllegalArgumentException("Stepper status is not valid")
            }
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        fun reservationVenue(
            classroom: Classroom,
            dateWithVenue: List<DateWithVenue>,
        ) {
            val username = authService.getAuthenticatedUser() ?: throw Exception(ErrorMessages.USER_NOT_FOUND)
            val user = userService.findByUsername(username)
            val owner = userService.findUserById(classroom.owner).username

            updateDateWithVenueClass(classroom.id!!, dateWithVenue)

            val context = Context()

            context.setVariable("username", staffUsername)
            context.setVariable("className", classroom.title)
            context.setVariable("profilePicture", user.profilePicture)
            context.setVariable("requester", user.username)
            context.setVariable("owners", owner)
            context.setVariable("description", classroom.details)
            context.setVariable("dateWithVenue", mapDateWithVenueToTemplate(dateWithVenue))
            context.setVariable("approveUrl", approveUrl)
            context.setVariable("rejectUrl", rejectUrl)

            mailService.sendEmail(
                subject = "[ClassCraft] Reservation venue for ${classroom.title} request from ${user.username}",
                template = "reservation", // reference to src/main/resources/templates/reservation.html
                context = context,
            )
        }

        fun searchClassByTitleOrDetails(keyword: String): List<Classroom> =
            classRepository.findByIsPublishedTrueAndTitleContainingIgnoreCaseOrDetailsContainingIgnoreCaseAndIsPublishedTrue(keyword, keyword)

        private fun updateUpdatedWhen(classroom: Classroom): Classroom {
            classroom.updatedWhen = LocalDateTime.now()
            return classroom
        }

        private fun validateCoOwners(coOwners: List<String>) {
            val errorList = mutableListOf<String>()
            coOwners.forEach {
                if (!userService.isUserExistById(it)) {
                    errorList.add(ErrorMessages.USER_WITH_ID_DOSE_NOT_EXIST.replace("\$0", it))
                }
            }
            if (errorList.isNotEmpty()) {
                throw Exception(errorList.joinToString(", "))
            }
        }

        private fun mapDateWithVenueToTemplate(dateWithVenue: List<DateWithVenue>): List<DateWithVenueDTO> =
            dateWithVenue.map {
                DateWithVenueDTO(
                    dates =
                        DateDetail(
                            startDateTime =
                                StartEndDetail(
                                    it.date.startDateTime
                                        .toLocalDate()
                                        .format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                                        .toString(),
                                    it.date.startDateTime
                                        .toLocalTime()
                                        .toString(),
                                ),
                            endDateTime =
                                StartEndDetail(
                                    it.date.endDateTime
                                        .toLocalDate()
                                        .toString(),
                                    it.date.endDateTime
                                        .toLocalTime()
                                        .toString(),
                                ),
                        ),
                    venue = it.venueId.map { venueService.findVenueById(it) },
                )
            }
    }
