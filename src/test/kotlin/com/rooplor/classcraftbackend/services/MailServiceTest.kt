package com.rooplor.classcraftbackend.services

import com.rooplor.classcraftbackend.services.mail.MailService
import jakarta.mail.internet.MimeMessage
import org.junit.jupiter.api.BeforeEach
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mail.javamail.JavaMailSender
import org.thymeleaf.TemplateEngine

@SpringBootTest
class MailServiceTest {
    @Mock
    private var javaMailSender: JavaMailSender? = null

    @Mock
    private lateinit var templateEngine: TemplateEngine

    @InjectMocks
    private lateinit var mailService: MailService

    private lateinit var mimeMessage: MimeMessage

    @BeforeEach
    fun setUp() {
        mimeMessage = mock(MimeMessage::class.java)
        `when`(javaMailSender?.createMimeMessage()).thenReturn(mimeMessage)
    }

//    @Test
//    fun `should send email successfully`(): Unit =
//        runBlocking {
//            // Arrange
//            val subject = "React 101"
//            val template = "testTemplate"
//            val context = Context()
//            val fromEmail = "sender@example.com"
//            val toEmail = "recipient@example.com"
//            val htmlContent = "<html><body> React 101 React 101 React 101 </body></html>"
//
//            // Mock @Value properties
//            setFinalField(mailService, "from", fromEmail)
//            setFinalField(mailService, "staffMail", toEmail)
//
//            // Stub the template engine to return predefined HTML content
//            `when`(templateEngine.process(template, context)).thenReturn(htmlContent)
//
//            // Act
//            mailService.sendEmail(subject, template, context)
//
//            // Assert
//            verify(javaMailSender)?.createMimeMessage()
// //            verify(templateEngine).process(template, context)
//
//            val helper = MimeMessageHelper(mimeMessage, true)
//            helper.setFrom(fromEmail)
//            helper.setTo(toEmail)
//            helper.setSubject(subject)
//            helper.setText(htmlContent, true)
//
//            // Verify that the email was sent
//            verify(javaMailSender)?.send(mimeMessage)
//        }
//
//    @Test
//    fun `should throw RuntimeException when email sending fails`() {
//        // Arrange
//        `when`(javaMailSender?.createMimeMessage()).thenThrow(RuntimeException("Mail server error"))
//
//        // Act & Assert
//        assertFailsWith<RuntimeException> {
//            runBlocking {
//                mailService.sendEmail("Subject", "template", Context())
//            }
//        }
//    }

    private fun setFinalField(
        target: Any,
        fieldName: String,
        value: Any,
    ) {
        val field = target::class.java.getDeclaredField(fieldName)
        field.isAccessible = true
        field.set(target, value)
    }
}
