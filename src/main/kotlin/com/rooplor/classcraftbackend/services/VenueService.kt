package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.entities.Classroom
import com.rooplor.classcraftbackend.entities.Venue
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.VenueRepository
import com.rooplor.classcraftbackend.services.mail.MailService
import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context

@AllArgsConstructor
@Service
class VenueService
    @Autowired
    constructor(
        private val venueRepository: VenueRepository,
        private val mailService: MailService,
        private val authService: AuthService,
        private val userService: UserService,
    ) {
        @Value("\${staff.username}")
        private val staffUsername: String? = null

        fun findAllVenue(): List<Venue> = venueRepository.findAll()

        fun insertVenue(addedVenue: Venue): Venue = venueRepository.insert(addedVenue)

        fun findVenueById(id: String): Venue = venueRepository.findById(id).orElseThrow()

        fun deleteClass(id: String) = venueRepository.deleteById(id)

        fun reserveVenue(
            classroom: Classroom,
            venueId: List<String>,
        ) {
            val username = authService.getAuthenticatedUser() ?: throw Exception(ErrorMessages.USER_NOT_FOUND)
            val user = userService.findByUsername(username)
            val owner = userService.findUserById(classroom.owner).username
            val context = Context()
            context.setVariable("username", staffUsername)
            context.setVariable("className", classroom.title)
            context.setVariable("profilePicture", user.profilePicture)
            context.setVariable("requestor", user.username)
            context.setVariable("owners", owner)
            context.setVariable("description", classroom.details)
            context.setVariable("date", classroom.date.map { it.toLocalDate().toString() })
            context.setVariable("time", classroom.date.map { it.toLocalTime().toString() })
            context.setVariable(
                "venue",
                removeBucketFromArray(
                    venueId
                        .map { findVenueById(it).name }
                        .toString(),
                ),
            )

            mailService.sendEmail(
                subject = "[ClassCraft] Reservation venue for ${classroom.title} request from ${user.username}",
                template = "reservation",
                context = context,
            )
        }

        private fun removeBucketFromArray(str: String) = str.replace("[", "").replace("]", "")
    }
