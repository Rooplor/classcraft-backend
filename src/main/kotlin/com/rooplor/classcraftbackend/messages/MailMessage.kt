package com.rooplor.classcraftbackend.messages

object MailMessage {
    const val REGISTRATION_SUBJECT = "Confirmation of your registration"
    const val TOPIC_REGISTRATION = "You’ve registered for "
    const val REGISTRATION_SUCCESS =
        "You’ve <span style=\"font-weight: bold; color: green;\">successfully</span> " +
            "registered for the class. We look forward to seeing you there!"
    const val REGISTRATION_PENDING =
        "Your registration is <span style=\"font-weight: bold; color: yellow;\">pending</span> " +
            "approval. We will notify you once it’s approved."
}
