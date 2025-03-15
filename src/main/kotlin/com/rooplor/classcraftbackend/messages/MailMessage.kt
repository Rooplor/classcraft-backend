package com.rooplor.classcraftbackend.messages

object MailMessage {
    const val REGISTRATION_SUBJECT = "[ClassCraft] Confirmation of your registration"
    const val REGISTRATION_TOPIC = "You’ve registered for "
    const val REGISTRATION_SUCCESS =
        "You’ve successfully registered for the class. We look forward to seeing you there!"
    const val REGISTRATION_PENDING =
        "Your registration is pending approval. We will notify you once it’s approved."

    const val CHECKIN_SUBJECT = "[ClassCraft] Check-in confirmation"
    const val CHECKIN_TOPIC = "You’ve checked in for "
    const val CHECKIN_SUCCESS = "You’ve successfully checked in for the class. Enjoy!"
    const val CHECKIN_PENDING = "Your check-in is pending approval. We will notify you once it’s approved."

    const val VENUE_STATUS_SUBJECT = "[ClassCraft] Venue status update on "
    const val VENUE_STATUS_TOPIC = "The status of your venue has been updated"
    const val VENUE_STATUS_APPROVED = "Your venue has been approved. Congratulations!"
    const val VENUE_STATUS_REJECTED = "Your venue has been rejected. with the following reason: "

    const val REGISTRATION_APPROVED_SUBJECT = "[ClassCraft] Your registration has been approved"
    const val REGISTRATION_PENDING_SUBJECT = "[ClassCraft] Your registration is pending approval"
    const val REGISTRATION_APPROVED_TOPIC = "Your registration has been approved for "
    const val REGISTRATION_PENDING_TOPIC = "Your registration is pending approval for "

    const val CLASS_DELETED_SUBJECT = "[ClassCraft] Class $0 has been deleted"
    const val CLASS_DELETED_TOPIC = "The class "
    const val CLASS_DELETED = " has been deleted. please contact the $0 for more information."

    const val CLASS_DELETED_VENUE_SUBJECT = "$CLASS_DELETED_SUBJECT Please cancel reservation for venue $1"

}
