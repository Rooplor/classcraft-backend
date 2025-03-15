package com.rooplor.classcraftbackend.messages

object MailMessage {
    const val REGISTRATION_SUBJECT = "Confirmation of your registration"
    const val REGISTRATION_TOPIC = "You’ve registered for "
    const val REGISTRATION_SUCCESS =
        "You’ve successfully registered for the class. We look forward to seeing you there!"
    const val REGISTRATION_PENDING =
        "Your registration is pending approval. We will notify you once it’s approved."
    const val CHECKIN_SUBJECT = "Check-in confirmation"
    const val CHECKIN_TOPIC = "You’ve checked in for "
    const val CHECKIN_SUCCESS = "You’ve successfully checked in for the class. Enjoy!"
    const val CHECKIN_PENDING = "Your check-in is pending approval. We will notify you once it’s approved."
}
