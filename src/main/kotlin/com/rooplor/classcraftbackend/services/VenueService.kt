package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.dtos.ReservationDTO
import com.rooplor.classcraftbackend.entities.Venue
import com.rooplor.classcraftbackend.repositories.VenueRepository
import com.rooplor.classcraftbackend.services.mail.MailService
import lombok.AllArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context

@AllArgsConstructor
@Service
class VenueService
    @Autowired
    constructor(
        private val venueRepository: VenueRepository,
        private val mailService: MailService,
    ) {
        fun findAllVenue(): List<Venue> = venueRepository.findAll()

        fun insertVenue(addedVenue: Venue): Venue = venueRepository.insert(addedVenue)

        fun findVenueById(id: String): Venue = venueRepository.findById(id).orElseThrow()

        fun deleteClass(id: String) = venueRepository.deleteById(id)

        fun reserveVenue(reservation: ReservationDTO) {
            val context = Context()
            context.setVariable(
                "username",
                "Anutra",
            )
            context.setVariable(
                "className",
                "Astro 101",
            )
            context.setVariable("profilePicture", "https://media.stickerswiki.app/oneesanstickers117/6747778.512.webp")
            context.setVariable(
                "requestor",
                "Naronkrach Tanajarusawas",
            )
            context.setVariable(
                "description",
                " สอนใช้ framework Astro ขั้นพื้นฐาน สอนใช้ framework Astro ขั้นพื้นฐานสอนใช้ framework Astro\n" +
                    "            ขั้นพื้นฐานสอนใช้ framework Astro ขั้นพื้นฐานสอนใช้ framework Astro ขั้นพื้นฐานสอนใช้ framework Astro\n" +
                    "            ขั้นพื้นฐาน",
            )
            context.setVariable(
                "date",
                "23 August 2024",
            )
            context.setVariable(
                "time",
                "10:00 - 12:00",
            )
            context.setVariable(
                "venue",
                "LX10 - 4, LX10 - 5",
            )

            mailService.sendEmail(
                to = "c3bosskung@gmail.com",
                subject = "Reservation",
                template = "reservation",
                context = context,
            )
        }
    }
