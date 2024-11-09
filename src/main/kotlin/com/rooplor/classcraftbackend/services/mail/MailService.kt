package com.rooplor.classcraftbackend.services.mail

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
        private val to: String? = null

        fun sendEmail(
            subject: String?,
            template: String?,
            context: Context?,
        ) {
            try {
                val mailMessage = javaMailSender!!.createMimeMessage()
                val helper = MimeMessageHelper(mailMessage, true)
                helper.setFrom(from!!)
                helper.setTo(to!!)
                helper.setSubject(subject!!)
                helper.setText("Hello", false)

                val htmlTemplate: String = templateEngine?.process(template, context) ?: ""
                helper.setText(htmlTemplate, true)
                javaMailSender.send(mailMessage)
            } catch (e: Exception) {
                throw RuntimeException(e.message)
            }
        }
    }
