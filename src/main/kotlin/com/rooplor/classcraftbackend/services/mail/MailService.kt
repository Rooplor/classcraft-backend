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
        ): String {
            try {
                val mailMessage = javaMailSender!!.createMimeMessage()
                val helper = MimeMessageHelper(mailMessage, true)
                helper.setFrom(from!!)
                helper.setTo(to!!)
                helper.setSubject(subject!!)
                helper.setText("Hello", false)

                val htmlTemplate: String = templateEngine?.process(template, context) ?: ""
                helper.setText(htmlTemplate, true)

                //            FileSystemResource file = new FileSystemResource(new File(System.getProperty("user.home") + "/Pictures/Untitled.jpeg    "));
//            helper.addAttachment(file.getFilename(), file);
                javaMailSender.send(mailMessage)
                return "Email sent successfully"
            } catch (e: Exception) {
                println(e.message)
                throw RuntimeException(e.message)
            }
        }
    }
