package com.rooplor.classcraftbackend.services.mail

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class MailService
    @Autowired
    constructor(
        private val javaMailSender: JavaMailSender,
        private val templateEngine: TemplateEngine,
    ) {
        @Value("\${spring.mail.username}")
        private val from: String? = null

        @Value("\${staff.email}")
        private val staffMail: String? = null

        @Value("\${staff.domain}")
        private val domain: String? = null

        fun sendEmail(
            subject: String?,
            template: String?,
            context: Context?,
            to: String? = staffMail,
        ) {
            try {
                val mailMessage = javaMailSender!!.createMimeMessage()
                val helper = MimeMessageHelper(mailMessage, true)
                helper.setFrom(from!!)
                helper.setTo(to!!)
                helper.setSubject(subject!!)

                val htmlTemplate: String = templateEngine?.process(template, context) ?: ""
                helper.setText(htmlTemplate, true)
                javaMailSender.send(mailMessage)
            } catch (e: Exception) {
                throw RuntimeException(e.message)
            }
        }

        fun announcementEmail(
            subject: String,
            topic: String,
            description: String,
            classroomId: String,
            to: String,
        ) {
            GlobalScope.launch {
                try {
                    val context = Context()
                    context.setVariable("context", topic)
                    context.setVariable("description", description)
                    context.setVariable("classroomLink", "$domain/class/$classroomId")
                    sendEmail(
                        subject,
                        "announcement",
                        context,
                        to,
                    )
                } catch (e: Exception) {
                    throw RuntimeException(e.message)
                }
            }
        }
    }
