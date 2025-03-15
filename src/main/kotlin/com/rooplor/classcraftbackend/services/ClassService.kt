package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.dtos.DateDetail
import com.rooplor.classcraftbackend.dtos.DateWithVenueDTO
import com.rooplor.classcraftbackend.dtos.StartEndDetail
import com.rooplor.classcraftbackend.entities.Classroom
import com.rooplor.classcraftbackend.entities.Form
import com.rooplor.classcraftbackend.enums.Status
import com.rooplor.classcraftbackend.enums.VenueStatus
import com.rooplor.classcraftbackend.helpers.ClassroomHelper
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.messages.MailMessage
import com.rooplor.classcraftbackend.repositories.ClassroomRepository
import com.rooplor.classcraftbackend.repositories.FormSubmissionRepository
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
        private val formSubmissionRepository: FormSubmissionRepository,
        private val venueService: VenueService,
        private val authService: AuthService,
        private val userService: UserService,
        private val mailService: MailService,
        private val formService: FormService,
        private val classroomHelper: ClassroomHelper,
    ) {
    @Value("\${staff.username}")
        private val staffUsername: String? = null

        @Value("\${staff.domain}")
        private val staffDomain: String? = null

        @Value("\${staff.email}")
        private val staffEmail: String? = null

        fun findAllClassPublishedWithRegistrationCondition(registrationStatus: Boolean): List<Classroom> =
            classRepository.findByRegistrationStatusAndIsPublishedTrueOrderByCreatedWhen(registrationStatus)

        fun findAllClassPublished(): List<Classroom> = classRepository.findByIsPublishedTrueOrderByCreatedWhen()

        fun insertClass(addedClassroom: Classroom): Classroom {
            classroomHelper.validateClassroom(addedClassroom)
            addedClassroom.registrationStatus = false
            addedClassroom.isPublished = false
            addedClassroom.owner = authService.getUserId()
            addedClassroom.stepperStatus = Status.RESERVE_VENUE.id
            addedClassroom.venueStatus = VenueStatus.NO_REQUEST.id
            val classroom = classRepository.insert(addedClassroom)
            val form =
                Form(
                    classroomId = classroom.id!!,
                    title = "Registration Form for ${classroom.title}",
                    description = classroom.details,
                    openDate = null,
                    closeDate = null,
                    fields = emptyList(),
                    isOwnerApprovalRequired = false,
                )
            formService.createForm(form)
            return classroom
        }

        fun updateClass(
            id: String,
            updatedClassroom: Classroom,
        ): Classroom {
            classroomHelper.validateClassroom(updatedClassroom)
            val classToUpdate = findClassById(id)
            isOwnerOfClass(classToUpdate)
            val originalDates = classToUpdate.dates
            val updatedDates = updatedClassroom.dates
            val isDateChange = originalDates != updatedDates
            if (classToUpdate.isPublished == true) {
                if (isDateChange) {
                    throw Exception(ErrorMessages.CLASS_CANNOT_CHANGE_DATE)
                }
            }
            if (classToUpdate.venueStatus == VenueStatus.APPROVED.id) {
                if (isDateChange) {
                    classToUpdate.venueStatus = VenueStatus.NO_REQUEST.id
                    formService.deleteFormSubmissionByFormId(classToUpdate.id!!)
                }
            }
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
            classToUpdate.classMaterials = updatedClassroom.classMaterials
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
            isOwnerOfClass(classToUpdate)
            classToUpdate.dates = dateWithVenue
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        fun updateMeetingUrlClass(
            id: String,
            meetingUrl: String,
        ): Classroom {
            val classToUpdate = findClassById(id)
            isOwnerOfClass(classToUpdate)
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
            isOwnerOfClass(classToUpdate)
            classToUpdate.content = classContent
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        fun updateRegistrationUrl(
            id: String,
            registrationUrl: String,
        ): Classroom {
            val classToUpdate = findClassById(id)
            isOwnerOfClass(classToUpdate)
            classToUpdate.registrationUrl = registrationUrl
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        fun setRegistrationStatus(
            id: String,
            status: Boolean,
        ): Classroom {
            val classToUpdate = findClassById(id)
            isOwnerOfClass(classToUpdate)
            classToUpdate.registrationStatus = status
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        fun updateVenueStatus(
            id: String,
            venueStatus: Int,
            rejectReason: String = "",
        ): Classroom {
            if (venueStatus == VenueStatus.REJECTED.id && rejectReason.isNullOrEmpty()) {
                throw IllegalArgumentException(ErrorMessages.VENUE_REJECT_REASON_IS_BLANK_OR_NULL)
            } else {
                val classToUpdate = findClassById(id)
                if (VenueStatus.values().contains(VenueStatus.values().find { it.id == venueStatus })) {
                    classToUpdate.venueStatus = VenueStatus.values().find { it.id == venueStatus }?.id
                    classToUpdate.rejectReason = rejectReason
                    if (venueStatus == VenueStatus.APPROVED.id || venueStatus == VenueStatus.REJECTED.id) {
                        mailService.announcementEmail(
                            subject = MailMessage.VENUE_STATUS_SUBJECT + "${classToUpdate.title}",
                            topic = MailMessage.VENUE_STATUS_TOPIC,
                            description = (if(venueStatus == VenueStatus.APPROVED.id) {
                                MailMessage.VENUE_STATUS_APPROVED
                            } else {
                                MailMessage.VENUE_STATUS_REJECTED + rejectReason
                            }).toString(),
                            classroomId = classToUpdate.id!!,
                            to = userService.findUserById(classToUpdate.owner).email,
                        )
                    }
                } else {
                    throw IllegalArgumentException(ErrorMessages.VENUE_STATUS_INVALID)
                }
                return classRepository.save(updateUpdatedWhen(classToUpdate))
            }
        }

        fun setPublishStatus(
            id: String,
            status: Boolean,
        ): Classroom {
            val classToUpdate = findClassById(id)
            isOwnerOfClass(classToUpdate)
            classToUpdate.isPublished = status
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

        fun deleteClass(id: String) {
            val userList = mutableListOf<String>()
            val classSubmission =  formSubmissionRepository.findByClassroomId(id)
            val owner = findClassById(id).owner
            classRepository.deleteById(id)
            classSubmission.forEach {
                userList.add(it.submittedBy!!)
            }
            userList.forEach {
                mailService.announcementEmail(
                    subject = MailMessage.CLASS_DELETED_SUBJECT.replace("\$0", findClassById(id).title),
                    topic = MailMessage.CLASS_DELETED_TOPIC,
                    description = MailMessage.CLASS_DELETED.replace("\$0", userService.findUserById(owner).email),
                    classroomId = id,
                    to = userService.findUserById(it).email,
                )
            }
            formService.deleteFormById(id)
            if (staffEmail != null) {
                mailService.announcementEmail(
                    subject = MailMessage.CLASS_DELETED_VENUE_SUBJECT.replace("\$0", findClassById(id).dates.flatMap { it.venueId.map { venueId -> venueService.findVenueById(venueId).room } }.joinToString(", ").trimEnd(',')),
                    topic = MailMessage.CLASS_DELETED_TOPIC,
                    description = MailMessage.CLASS_DELETED.replace("\$0", userService.findUserById(owner).email),
                    classroomId = id,
                    to = staffEmail ,
                )
            }
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
            isOwnerOfClass(classroom)
            val username = authService.getAuthenticatedUser() ?: throw Exception(ErrorMessages.USER_NOT_FOUND)
            val user = userService.findByUsername(username)
            val owner = userService.findUserById(classroom.owner).username

            val context = Context()

            context.setVariable("username", staffUsername)
            context.setVariable("className", classroom.title)
            context.setVariable("profilePicture", user.profilePicture)
            context.setVariable("requester", user.username)
            context.setVariable("owners", owner)
            context.setVariable("description", classroom.details)
            context.setVariable("dateWithVenue", mapDateWithVenueToTemplate(dateWithVenue))
            context.setVariable("approveUrl", staffDomain + "/class/${classroom.id}/reservation/approved")
            context.setVariable("rejectUrl", staffDomain + "/class/${classroom.id}/reservation/rejected")

            mailService.sendEmail(
                subject = "[ClassCraft] Reservation venue for ${classroom.title} request from ${user.username}",
                template = "reservation", // reference to src/main/resources/templates/reservation.html
                context = context,
            )

            updateDateWithVenueClass(classroom.id!!, dateWithVenue)
            updateStepperStatus(classroom.id!!, Status.CRAFT_CONTENT.id)
            updateVenueStatus(classroom.id!!, VenueStatus.PENDING.id)
        }

        fun searchClassByTitleOrDetails(keyword: String): List<Classroom> =
            classRepository.findByIsPublishedTrueAndTitleContainingIgnoreCaseOrDetailsContainingIgnoreCaseAndIsPublishedTrue(
                keyword,
                keyword,
            )

        fun updateClassMaterials(
            id: String,
            classMaterials: List<String>,
        ): Classroom {
            val classToUpdate = findClassById(id)
            isOwnerOfClass(classToUpdate)
            classToUpdate.classMaterials = classMaterials
            return classRepository.save(updateUpdatedWhen(classToUpdate))
        }

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

        private fun isOwnerOfClass(classroom: Classroom) {
            val username = authService.getAuthenticatedUser() ?: throw Exception(ErrorMessages.USER_NOT_FOUND)
            val user = userService.findByUsername(username)
            if (classroom.owner != user.id) {
                throw Exception(ErrorMessages.FORBIDDEN)
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
                                        .plusHours(7) // need to support another timezone in the future
                                        .toLocalDate()
                                        .format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                                        .toString(),
                                    it.date.startDateTime
                                        .plusHours(7) // need to support another timezone in the future
                                        .toLocalTime()
                                        .format(DateTimeFormatter.ofPattern("HH:mm"))
                                        .toString(),
                                ),
                            endDateTime =
                                StartEndDetail(
                                    it.date.endDateTime
                                        .plusHours(7) // need to support another timezone in the future
                                        .toLocalDate()
                                        .format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                                        .toString(),
                                    it.date.endDateTime
                                        .plusHours(7) // need to support another timezone in the future
                                        .toLocalTime()
                                        .format(DateTimeFormatter.ofPattern("HH:mm"))
                                        .toString(),
                                ),
                        ),
                    venue = it.venueId.map { venueService.findVenueById(it) },
                )
            }
    }
